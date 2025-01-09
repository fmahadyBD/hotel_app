package com.f.backend.service;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.f.backend.entity.Token;
import com.f.backend.entity.User;
import com.f.backend.enums.Role;
import com.f.backend.jwt.JwtService;
import com.f.backend.reposiotry.TokenRepository;
import com.f.backend.reposiotry.UserRepository;
import com.f.backend.response.AuthenticationResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

// The AuthService class handles user authentication, token management, and registration processes.
@Service
public class AuthService {

    @Value("${image.upload.dir}")
    private String uploadDir;

    // Dependencies for user management, password encoding, JWT, token storage, and
    // email services.
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    // Constructor injection to initialize the dependencies.
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
            TokenRepository tokenRepository, AuthenticationManager authenticationManager, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
    }

    /**
     * Saves a newly generated JWT token for a user in the database.
     *
     * @param jwt  the JWT token to be saved
     * @param user the user associated with the token
     */
    private void savedUserToken(String jwt, User user) {
        Token token = new Token(); // Create a new Token entity
        token.setToken(jwt); // Set the JWT token
        token.setUser(user); // Associate the token with the user
        token.setLogout(false); // Mark the token as active (not logged out)
        tokenRepository.save(token); // Save the token in the repository
    }

    /**
     * Marks all tokens associated with a user as logged out (invalidates them).
     *
     * @param user the user whose tokens need to be invalidated
     */
    private void removeAllTokenByUser(User user) {
        // Find all tokens for the user that are still valid
        List<Token> validTokens = tokenRepository.findAllTokenByUser(user.getId());

        // If no valid tokens exist, return without further action
        if (validTokens.isEmpty()) {
            return;
        }

        // Mark each valid token as logged out
        validTokens.forEach(t -> {
            t.setLogout(true);
        });

        // Save the updated token states back to the repository
        tokenRepository.saveAll(validTokens);
    }

    /**
     * Handles user registration by saving the user details, encoding the password,
     * generating a JWT, saving the token, and returning an authentication response.
     *
     * @param imageFile
     * @param user      the user to be registered
     * @return an AuthenticationResponse containing token and user details
     */
    public AuthenticationResponse register(MultipartFile imageFile, User user) {
        // Currently, the method returns null (to be implemented later)

        try {
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                return new AuthenticationResponse(null, "User Already Exists");

            }
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageFileName = saveImage(imageFile, user);
                user.setImage(imageFileName);
            }

            // user password to save DB
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole(Role.valueOf("USER"));
            user.setLock(true);
            user.setActive(false);

            userRepository.save(user);
            String jwt = jwtService.generateToken(user);

            savedUserToken(jwt, user);

            sendActivationEmail(user);
            return new AuthenticationResponse(jwt, "User Registration wast Successful");

        } catch (Exception e) {
            return new AuthenticationResponse(null, "Registration failed due to internal error");
        }

    }

    private void sendActivationEmail(User user) {

        String activationLink = "http://localhost:8089/active/" + user.getId();
        String mailText = " <h2> Dear </h2> " + user.getName() + ","
                + "<p>Pls Click on the following link to confirm your registration </p>"
                + "<a href=\"" + activationLink + "\">Active Account</a>";
        String subject = "Confirm Registration";
        try {
            emailService.sendSimpleEmail(user.getEmail(), subject, mailText);
        } catch (MessagingException e) {
            throw new RuntimeException();

        }

    }

    public AuthenticationResponse authetecate(User request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        String jwt = jwtService.generateToken(user);

        // Remove all existing token for this user
        removeAllTokenByUser(user);// logout
        savedUserToken(jwt, user);
        return new AuthenticationResponse(jwt, "User login Successful");

    }

    public String activeUser(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User Not found with this id: " + id));

        if (user != null) {
            user.setActive(true);
            userRepository.save(user);
            return "User active Successfully";
        } else {
            return "Invalid activation token";
        }
    }

    public String saveImage(MultipartFile image, User user) throws IOException {

        Path uploadPath = Paths.get(uploadDir + "/users");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String fileName = user.getName() + "_" + UUID.randomUUID();

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(image.getInputStream(), filePath);
        return fileName;

    }
}
