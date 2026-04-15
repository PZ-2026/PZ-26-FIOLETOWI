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
        System.out.println("----- LOGIN ATTEMPT -----");
        System.out.println("Email: '" + request.getEmail() + "'");
        System.out.println("Password: '" + request.getPassword() + "'");

        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            System.out.println("Found User: " + user.getUsername());
            System.out.println("DB Password Hash: " + user.getPasswordHash());
            boolean match = BCrypt.checkpw(request.getPassword(), user.getPasswordHash());
            System.out.println("BCrypt Match: " + match);

            if (match) {
                return new AuthResponse("Login successful", user.getUsername(), user.getEmail(), user.getRole());
            }
        } else {
            System.out.println("User not found in database!");
            
            // Print all users in DB to debug
            System.out.println("Users currently in DB:");
            userRepository.findAll().forEach(u -> 
                System.out.println(" - " + u.getEmail() + " | " + u.getPasswordHash())
            );
        }
        throw new RuntimeException("Invalid email or password");
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
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
