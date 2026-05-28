package com.example.movierate_backend.dto;

public class AdminUserResponse {
    private final Long id;
    private final String username;
    private final String email;
    private final String role;
    private final boolean blocked;
    private final String createdAt;

    public AdminUserResponse(Long id, String username, String email, String role, boolean blocked, String createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.blocked = blocked;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public boolean isBlocked() { return blocked; }
    public String getCreatedAt() { return createdAt; }
}
