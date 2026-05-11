package com.example.movierate_backend.dto;

public class AuthResponse {
    private String message;
    private String username;
    private String email;
    private String role;
    private String createdAt;
    
    public AuthResponse(String message, String username, String email, String role, String createdAt) {
        this.message = message;
        this.username = username;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
    }

    public String getMessage() { return message; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getCreatedAt() { return createdAt; }
}
