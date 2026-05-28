package com.example.movierate_backend.controller;

import com.example.movierate_backend.dto.AuthResponse;
import com.example.movierate_backend.dto.LoginRequest;
import com.example.movierate_backend.dto.RegisterRequest;
import com.example.movierate_backend.dto.UpdateProfileRequest;
import com.example.movierate_backend.model.User;
import com.example.movierate_backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestParam Long userId, @Valid @RequestBody UpdateProfileRequest request) {
        try {
            User updated = authService.updateProfile(userId, request);
            AuthResponse response = new AuthResponse(
                    updated.getId(),
                    "Profil zaktualizowany",
                    updated.getUsername(),
                    updated.getEmail(),
                    updated.getRole(),
                    updated.getCreatedAt() != null ? updated.getCreatedAt().toString() : null,
                    updated.getProfilePictureUrl()
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationErrors(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .distinct()
                .collect(Collectors.joining(". "));

        return ResponseEntity.badRequest().body(message);
    }
}
