package com.example.movierate_backend.dto;

/**
 * Odpowiedź uwierzytelniania przesyłana do klienta.
 */
public class AuthResponse {
    private Long userId;
    private String message;
    private String username;
    private String email;
    private String role;
    private String createdAt;
    private String profilePictureUrl;
    private Boolean blocked = false;
    
    /**
     * Domyślny konstruktor niezbędny dla serializacji.
     */
    public AuthResponse() {}

    /**
     * Konstruktor z podstawowymi parametrami użytkownika.
     * @param userId ID użytkownika
     * @param message wiadomość z serwera (np. status operacji)
     * @param username nazwa użytkownika
     * @param email e-mail użytkownika
     * @param role przypisana rola
     * @param createdAt data utworzenia konta
     */
    public AuthResponse(Long userId, String message, String username, String email, String role, String createdAt) {
        this.userId = userId;
        this.message = message;
        this.username = username;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
    }

    /**
     * Konstruktor ze wszystkimi parametrami, łącznie z awatarem.
     * @param userId ID użytkownika
     * @param message wiadomość z serwera
     * @param username nazwa użytkownika
     * @param email e-mail
     * @param role przypisana rola
     * @param createdAt data rejestracji
     * @param profilePictureUrl adres URL do profilowego zdjęcia
     */
    public AuthResponse(Long userId, String message, String username, String email, String role, String createdAt, String profilePictureUrl) {
        this.userId = userId;
        this.message = message;
        this.username = username;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
        this.profilePictureUrl = profilePictureUrl;
    }

    public AuthResponse(Long userId, String message, String username, String email, String role, String createdAt, String profilePictureUrl, Boolean blocked) {
        this(userId, message, username, email, role, createdAt, profilePictureUrl);
        this.blocked = blocked;
    }

    /**
     * Zwraca ID użytkownika.
     * @return identyfikator
     */
    public Long getUserId() { return userId; }
    /**
     * Ustawia ID użytkownika.
     * @param userId nowe ID
     */
    public void setUserId(Long userId) { this.userId = userId; }

    /**
     * Zwraca powiązaną wiadomość np. powitalną.
     * @return treść wiadomości
     */
    public String getMessage() { return message; }
    /**
     * Ustawia wiadomość z serwera.
     * @param message treść
     */
    public void setMessage(String message) { this.message = message; }

    /**
     * Zwraca nazwę konta.
     * @return login/username
     */
    public String getUsername() { return username; }
    /**
     * Ustawia nazwę użytkownika.
     * @param username nazwa użytkownika
     */
    public void setUsername(String username) { this.username = username; }

    /**
     * Zwraca adres email.
     * @return email
     */
    public String getEmail() { return email; }
    /**
     * Przypisuje adres e-mail.
     * @param email adres
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Pobiera rolę przypisaną do konta.
     * @return rola
     */
    public String getRole() { return role; }
    /**
     * Ustawia rolę.
     * @param role nowa rola
     */
    public void setRole(String role) { this.role = role; }

    /**
     * Zwraca sformatowaną datę założenia konta.
     * @return data utworzenia
     */
    public String getCreatedAt() { return createdAt; }
    /**
     * Ustawia datę założenia konta.
     * @param createdAt data utworzenia
     */
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    /**
     * Zwraca adres do zdjęcia profilowego.
     * @return URL awataru
     */
    public String getProfilePictureUrl() { return profilePictureUrl; }
    /**
     * Ustawia profilowe zdjęcie.
     * @param profilePictureUrl URL awatara
     */
    public void setProfilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; }

    public Boolean getBlocked() { return blocked; }
    public void setBlocked(Boolean blocked) { this.blocked = blocked; }
}
