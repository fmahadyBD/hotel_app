package com.f.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.f.backend.custom_exception.UserNotFoundException;
import com.f.backend.entity.User;
import com.f.backend.reposiotry.UserRepository;
import com.f.backend.response.AuthenticationResponse;
import com.f.backend.service.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.List;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    /**
     * @param user
     * @param file
     * @return
     * @throws JsonProcessingException Post: URL
     *                                 form-data
     *                                 key value Content type
     *                                 user json data application/json
     *                                 image give the file multipart/form-data
     */

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestPart(value = "user") User user,
            @RequestParam(value = "image") MultipartFile file) throws IOException {
        return ResponseEntity.ok(authService.register(file, user));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody User request) {
        return ResponseEntity.ok(authService.authetecate(request));
    }

    @GetMapping("/active/{id}")
    public ResponseEntity<String> activeUser(@PathVariable("id") long id) {
        try {
            String response = authService.activeUser(id);
            return ResponseEntity.ok(response);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @GetMapping("/all")
    public List<User> getAllUser() {
        return userRepository.findAll();
    }
}
