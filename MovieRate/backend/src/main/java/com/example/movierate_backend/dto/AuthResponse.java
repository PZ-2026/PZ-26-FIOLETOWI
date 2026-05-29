package com.example.movierate_backend.dto;

public class AuthResponse {
    private Long userId;
    private String message;
    private String username;
    private String email;
    private String role;
    private String createdAt;
    private String profilePictureUrl;
    
    public AuthResponse() {}

    public AuthResponse(Long userId, String message, String username, String email, String role, String createdAt) {
        this.userId = userId;
        this.message = message;
        this.username = username;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
    }

    public AuthResponse(Long userId, String message, String username, String email, String role, String createdAt, String profilePictureUrl) {
        this.userId = userId;
        this.message = message;
        this.username = username;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
        this.profilePictureUrl = profilePictureUrl;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getProfilePictureUrl() { return profilePictureUrl; }
    public void setProfilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; }
}
