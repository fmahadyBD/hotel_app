package com.f.backend.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.f.backend.entity.User;
import com.f.backend.reposiotry.UserRepository;
import com.f.backend.response.AuthenticationResponse;
import com.f.backend.service.AuthService;

@RestController
public class AuthenController {

    @Autowired
    private AuthService authService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestPart(value = "user") User user,
            @RequestParam(value = "image") MultipartFile file) throws IOException {

        return ResponseEntity.ok(authService.register(user,file));

    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody User request) {
        return ResponseEntity.ok(authService.authencate(request));
    }

}
