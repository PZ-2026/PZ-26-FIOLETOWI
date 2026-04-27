package com.example.movierate_backend.service;

import com.example.movierate_backend.dto.AuthResponse;
import com.example.movierate_backend.dto.LoginRequest;
import com.example.movierate_backend.dto.RegisterRequest;
import com.example.movierate_backend.model.User;
import com.example.movierate_backend.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AuthResponse login(LoginRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            boolean match = BCrypt.checkpw(request.getPassword(), user.getPasswordHash());

            if (match) {
                return new AuthResponse("Login successful", user.getUsername(), user.getEmail(), user.getRole());
            }
        }

        throw new IllegalArgumentException("Invalid email or password");
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        String hashedPassword = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());
        user.setPasswordHash(hashedPassword);
        user.setRole("USER"); // Default role

        userRepository.save(user);

        return new AuthResponse("Registration successful", user.getUsername(), user.getEmail(), user.getRole());
    }
}
