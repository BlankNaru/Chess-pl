package org.example.chessserver.databaseClasses.controllers;

import org.example.chessserver.databaseClasses.dto.AuthResponse;
import org.example.chessserver.databaseClasses.dto.LoginRequest;
import org.example.chessserver.databaseClasses.dto.RegisterRequest;
import org.example.chessserver.databaseClasses.entities.User;
import org.example.chessserver.databaseClasses.repositories.UserRepository;
import org.example.chessserver.databaseClasses.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Username already taken"));
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Email already used"));
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());


        userRepository.save(user);

        return ResponseEntity.ok(Collections.singletonMap("message", "User created"));
    }
}
