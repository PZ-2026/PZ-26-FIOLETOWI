package com.example.movierate_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO służący do zwalidowania i przeprowadzania operacji edytowania informacji podstawowych na profilu.
 */
public class UpdateProfileRequest {
    @NotBlank(message = "Nazwa użytkownika jest wymagana")
    @Size(min = 3, max = 50, message = "Nazwa użytkownika musi mieć od 3 do 50 znaków")
    private String username;

    @NotBlank(message = "Email jest wymagany")
    @Email(message = "Nieprawidłowy format email")
    private String email;

    private String password;

    private String profilePictureUrl;

    /**
     * Pobiera wprowadzaną nazwę nowej tożsamości.
     * @return nowa nazwa
     */
    public String getUsername() { return username; }
    
    /**
     * Ustawia podmienianą nazwę w klasie.
     * @param username string login
     */
    public void setUsername(String username) { this.username = username; }
    
    /**
     * Pobiera zmieniany format poczty e-mail.
     * @return adres email
     */
    public String getEmail() { return email; }
    
    /**
     * Podmienia zmienny format kontaktowy konta.
     * @param email wpisywana wartość docelowa z formularza e-mailowego
     */
    public void setEmail(String email) { this.email = email; }
    
    /**
     * Pobiera zmieniany z pola tekstowego układ uwierzytelniania w logowaniu.
     * @return zmieniane hasło 
     */
    public String getPassword() { return password; }
    
    /**
     * Podmienia kodowanie logowania wprowadzonym nowym kodowaniem od gracza na bazie żądania resetu i aktualizacji.
     * @param password odnowiony wariant autentykacyjny
     */
    public void setPassword(String password) { this.password = password; }
    
    /**
     * Pobiera wpis ze zwrotem obrazu serwera ze zdjęcia wgranej personalizacji wyglądu (av).
     * @return aktualny układ adresu zdjęcia
     */
    public String getProfilePictureUrl() { return profilePictureUrl; }
    
    /**
     * Określa docelowo zmianę referencyjną i lokalizacyjną wizualnego akcentowania panelu (zmiana miniaturki u gracza i wpisach).
     * @param profilePictureUrl pole url dla wglądu estetyki i edycji elementu w profilu
     */
    public void setProfilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; }
}
