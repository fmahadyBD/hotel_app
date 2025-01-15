package com.f.backend.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
import com.f.backend.response.Request;

@Service
public class AuthService {

    @Value("${image.upload.dir}")
    private String uploadDir;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public AuthService(UserRepository uuRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
            TokenRepository tokenRepository, AuthenticationManager authenticationManager, EmailService emailService) {
        this.userRepository = uuRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
    }

    public AuthenticationResponse register(MultipartFile file, User user) {
        try {
            if (userRepository.findByEmail(user.getUsername()).isPresent()) {
                return new AuthenticationResponse(null, "UserAlready Exists");
            }
            if (file != null && !file.isEmpty()) {
                String imageFileName = savedImage(file, user);
                user.setImage(imageFileName);
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole(Role.valueOf("USER"));
            user.setLock(true);
            user.setActive(false);
            userRepository.save(user);

            String jwt = jwtService.generateToken(user);
            savedUserToken(jwt, user);
            return new AuthenticationResponse(jwt, "User registration successfully");

        } catch (Exception e) {

            return new AuthenticationResponse(null, "Registration failed due to internal error: " + e.getMessage());
        }
    }

    // public AuthenticationResponse authetecate(User request) {
    // authenticationManager.authenticate(
    // new UsernamePasswordAuthenticationToken(request.getUsername(),
    // request.getPassword()));
    // User user = userRepository.findByEmail(request.getUsername()).orElseThrow();
    // String jwt = jwtService.generateToken(user);

    // removeAllTokenByUser(user);
    // savedUserToken(jwt, user);
    // return new AuthenticationResponse(jwt, "User login Successfully");
    // }

    public AuthenticationResponse authenticate(User request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        System.out.println("Authentication successful for: " + request.getUsername());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        System.out.println("User retrieved: " + user.getEmail());

        String jwt = jwtService.generateToken(user);
        System.out.println("Generated JWT: " + jwt);

        // removeAllTokenByUser(user);
        // savedUserToken(jwt, user);
        return new AuthenticationResponse(jwt, "User login Successfully");
    }

    private void savedUserToken(String jwt, User user) {
        Token token = new Token();
        token.setUser(user);
        token.setToken(jwt);
        token.setLogout(false);
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

    public String savedImage(MultipartFile file, User user) throws IOException {
        Path uploadPath = Paths.get(uploadDir, "/users");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String fileName = user.getUsername() + "_" + UUID.randomUUID();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);
        return fileName;
    }

}
