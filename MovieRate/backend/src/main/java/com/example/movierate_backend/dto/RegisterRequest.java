package com.example.movierate_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Model przekazywany z parametrami podczas próby zarejestrowania nowej osoby do platformy.
 */
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

    /**
     * Pobiera żądaną nazwę dla konta.
     * @return wymyślony pseudonim
     */
    public String getUsername() { return username; }
    
    /**
     * Ustawia nazwę użytkownika.
     * @param username string powiązany z imieniem profilowym
     */
    public void setUsername(String username) { this.username = username; }

    /**
     * Zwraca wpisany podczas rejestracji kontakt.
     * @return email adresowy użytkownika
     */
    public String getEmail() { return email; }
    
    /**
     * Ustawia pocztę.
     * @param email wprowadzony kontakt
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Pobiera zabezpieczenie autoryzacyjne.
     * @return wpisane hasło (które ulegnie zahashowaniu)
     */
    public String getPassword() { return password; }
    
    /**
     * Ustawia autoryzacyjny ciąg znaków użytkownika.
     * @param password nowo nadane hasło
     */
    public void setPassword(String password) { this.password = password; }
}
