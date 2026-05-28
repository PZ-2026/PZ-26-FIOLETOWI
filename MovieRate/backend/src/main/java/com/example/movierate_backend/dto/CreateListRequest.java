package com.example.movierate_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateListRequest {
    @NotNull(message = "ID użytkownika jest wymagane")
    private Long userId;

    @NotBlank(message = "Nazwa listy jest wymagana")
    private String name;

    @NotBlank(message = "Typ listy jest wymagany")
    private String type;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
