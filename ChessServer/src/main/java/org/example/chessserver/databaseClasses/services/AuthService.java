package org.example.chessserver.databaseClasses.services;

import org.example.chessserver.databaseClasses.dto.AuthResponse;
import org.example.chessserver.databaseClasses.dto.LoginRequest;
import org.example.chessserver.databaseClasses.entities.User;
import org.example.chessserver.databaseClasses.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository repo;
    private final JwtService jwt;

    public AuthService(UserRepository repo, JwtService jwt) {
        this.repo = repo;
        this.jwt = jwt;
    }

    public AuthResponse login(LoginRequest req) {
        User user = repo.findByUsername(req.username())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!user.getPassword().equals(req.password()))
        {
            throw new RuntimeException("Invalid password");
        }

        String token = jwt.generateToken(user.getUsername());

        return new AuthResponse(token, user.getUsername());
    }
}
