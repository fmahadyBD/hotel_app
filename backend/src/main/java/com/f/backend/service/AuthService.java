package com.f.backend.service;

import jakarta.mail.MessagingException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
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

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    @Value("${image.upload.dir}")
    private String uploadDir;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
            TokenRepository tokenRepository, AuthenticationManager authenticationManager, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
    }

    private void saveUserToken(String jwt, User user) {
        Token token = new Token();
        token.setToken(jwt);
        token.setLogout(false);
        token.setUser(user);

        tokenRepository.save(token);

    }

    private void removeAllTokenByUser(User user) {

        List<Token> validTokens = tokenRepository.findAllTokenByUser(user.getId());

        if (validTokens.isEmpty()) {
            return;
        }
        validTokens.forEach(t -> {
            t.setLogout(true);
        });
        tokenRepository.saveAll(validTokens);

    }

    public AuthenticationResponse register(User user, MultipartFile imageFile) throws IOException {
        // We check that Already any user Exists with this email
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {

            return new AuthenticationResponse(null, "User Already Exists");
        }
        String imageFileName = "";

        if (imageFile != null && !imageFile.isEmpty()) {
            imageFileName = savedImage(imageFile, user);

        }

        // Encode user password to save DB
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.valueOf("USER"));
        user.setLock(true);
        user.setActive(false);
        user.setImage(imageFileName);

        userRepository.save(user);
        String jwt = jwtService.generateToken(user);
        saveUserToken(jwt, user);

        // sendActivationEmail(user);

        return new AuthenticationResponse(jwt, "User Registration was Successful");
    }

    private void sendActivationEmail(User user) {
        String activationLink = "http://localhost:8080/active/" + user.getId();
        String mailText = "<h2>Dear " + user.getName() + ",</h2>"
                + "<p>Please click on the following link to confirm your registration:</p>"
                + "<a href=\"" + activationLink + "\">Activate Account</a>";
        String subject = "Confirm Registration";
        try {
            emailService.sendSimpleEmail(user.getEmail(), subject, mailText);
        } catch (MessagingException e) {
            throw new RuntimeException();

        }

    }

    public AuthenticationResponse authencate(User request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        // Generate Token for Current User
        String jwt = jwtService.generateToken(user);

        // Remove all existing toke for this user
        removeAllTokenByUser(user);

        saveUserToken(jwt, user);

        return new AuthenticationResponse(jwt, "User Login Successful");

    }

    public String activeUser(int id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not Found with this ID " + id));

        if (user != null) {
            user.setActive(true);
            userRepository.save(user);
            return "User Activated Successfully!";
        } else {
            return "Invalid Activation Token!";
        }

    }

    private String savedImage(MultipartFile file, User user) throws IOException {
        Path uploadPath = Paths.get(uploadDir, "users");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String fileName = user.getName() + "_" + UUID.randomUUID();
        Path filePath = uploadPath.resolve(fileName);

        Files.copy(file.getInputStream(), filePath);
        return fileName;

    }

}
