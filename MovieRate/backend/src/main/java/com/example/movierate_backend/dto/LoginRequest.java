package com.example.movierate_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Reprezentuje żądanie logowania użytkownika.
 */
public class LoginRequest {
    @NotBlank(message = "Podaj adres e-mail")
    @Email(message = "Podaj poprawny adres e-mail")
    private String email;

    @NotBlank(message = "Podaj haslo")
    private String password;

    /**
     * Pobiera adres e-mail użytkownika.
     * @return adres e-mail
     */
    public String getEmail() { return email; }

    /**
     * Ustawia adres e-mail użytkownika.
     * @param email adres e-mail
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Pobiera hasło użytkownika.
     * @return hasło użytkownika
     */
    public String getPassword() { return password; }

    /**
     * Ustawia hasło użytkownika.
     * @param password hasło użytkownika
     */
    public void setPassword(String password) { this.password = password; }
}
