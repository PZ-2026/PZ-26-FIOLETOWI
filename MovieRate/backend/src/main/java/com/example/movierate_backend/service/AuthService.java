package com.example.movierate_backend.service;

import com.example.movierate_backend.dto.AuthResponse;
import com.example.movierate_backend.dto.LoginRequest;
import com.example.movierate_backend.dto.RegisterRequest;
import com.example.movierate_backend.dto.UpdateProfileRequest;
import com.example.movierate_backend.model.User;
import com.example.movierate_backend.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AuthResponse login(LoginRequest request) {
        logger.trace("Received login request");

        String email = request.getEmail().trim().toLowerCase();
        String maskedEmail = maskEmail(email);
        logger.debug("Normalized login email={}", maskedEmail);

        Optional<User> optionalUser;
        try {
            optionalUser = userRepository.findByEmail(email);
        } catch (RuntimeException exception) {
            logger.error("Login failed while looking up user email={}", maskedEmail, exception);
            throw exception;
        }

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            logger.trace("Found user account for login email={}", maskedEmail);

            boolean match = BCrypt.checkpw(request.getPassword(), user.getPasswordHash());

            if (match) {
                logger.info("User login successful email={} role={}", maskedEmail, user.getRole());
                return new AuthResponse(
                        user.getId(),
                        "Login successful",
                        user.getUsername(),
                        user.getEmail(),
                        user.getRole(),
                        user.getCreatedAt().toString(),
                        user.getProfilePictureUrl()
                );
            }

            logger.warn("Login failed because password did not match email={}", maskedEmail);
        } else {
            logger.warn("Login failed because user was not found email={}", maskedEmail);
        }

        throw new IllegalArgumentException("Nieprawidlowy adres e-mail lub haslo");
    }

    public AuthResponse register(RegisterRequest request) {
        logger.trace("Received registration request");

        String email = request.getEmail().trim().toLowerCase();
        String username = request.getUsername().trim();
        String maskedEmail = maskEmail(email);
        logger.debug("Normalized registration data username={} email={}", username, maskedEmail);

        Optional<User> existingUser;
        try {
            existingUser = userRepository.findByEmail(email);
        } catch (RuntimeException exception) {
            logger.error("Registration failed while checking existing user email={}", maskedEmail, exception);
            throw exception;
        }

        if (existingUser.isPresent()) {
            logger.warn("Registration failed because email is already taken email={}", maskedEmail);
            throw new IllegalArgumentException("Adres e-mail jest juz zajety");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        logger.trace("Hashing password for new user email={}", maskedEmail);
        String hashedPassword = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());
        user.setPasswordHash(hashedPassword);
        user.setRole("USER"); // Default role

        User savedUser;
        try {
            savedUser = userRepository.save(user);
            logger.info("User registration successful email={} role={}", maskedEmail, savedUser.getRole());
        } catch (RuntimeException exception) {
            logger.error("Registration failed while saving user email={}", maskedEmail, exception);
            throw exception;
        }

        return new AuthResponse(
                savedUser.getId(),
                "Registration successful",
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getRole(),
                savedUser.getCreatedAt().toString(),
                null
        );
    }

    public User updateProfile(Long userId, UpdateProfileRequest request) {
        logger.trace("Updating profile for userId={}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.warn("Profile update failed: user not found id={}", userId);
                    return new IllegalArgumentException("Użytkownik nie znaleziony");
                });

        // Check if email is already taken by another user
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail().trim().toLowerCase());
        if (existingUser.isPresent() && !existingUser.get().getId().equals(userId)) {
            logger.warn("Profile update failed: email already taken email={}", maskEmail(request.getEmail()));
            throw new IllegalArgumentException("Adres e-mail jest już zajęty");
        }

        user.setUsername(request.getUsername().trim());
        user.setEmail(request.getEmail().trim().toLowerCase());

        // Update password if provided
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            String hashedPassword = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());
            user.setPasswordHash(hashedPassword);
            logger.trace("Password updated for userId={}", userId);
        }

        // Update profile picture URL if provided
        if (request.getProfilePictureUrl() != null) {
            user.setProfilePictureUrl(request.getProfilePictureUrl());
            logger.trace("Profile picture updated for userId={}", userId);
        }

        User savedUser = userRepository.save(user);
        logger.info("Profile updated successfully userId={}", userId);
        return savedUser;
    }

    private String maskEmail(String email) {
        int atIndex = email.indexOf('@');
        if (atIndex <= 1) {
            return "***" + email.substring(Math.max(atIndex, 0));
        }

        String localPart = email.substring(0, atIndex);
        String domain = email.substring(atIndex);
        return localPart.charAt(0) + "***" + domain;
    }
}
