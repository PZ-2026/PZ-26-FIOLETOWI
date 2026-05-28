package com.example.movierate_backend.dto;

public class CastMemberResponse {
    private final String name;
    private final String role;

    public CastMemberResponse(String name, String role) {
        this.name = name;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }
}
