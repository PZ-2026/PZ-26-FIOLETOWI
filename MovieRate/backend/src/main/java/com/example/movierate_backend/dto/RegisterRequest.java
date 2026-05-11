package com.example.movierate_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank(message = "Podaj nazwe uzytkownika")
    @Size(min = 3, max = 50, message = "Nazwa uzytkownika musi miec od 3 do 50 znakow")
    private String username;

    @NotBlank(message = "Podaj adres e-mail")
    @Email(message = "Podaj poprawny adres e-mail")
    private String email;

    @NotBlank(message = "Podaj haslo")
    @Size(min = 8, max = 100, message = "Haslo musi miec co najmniej 8 znakow")
    private String password;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
