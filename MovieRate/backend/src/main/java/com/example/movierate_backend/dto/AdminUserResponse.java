package com.example.movierate_backend.dto;

/**
 * Odpowiedź zawierająca szczegółowe dane użytkownika dla panelu administratora.
 */
public class AdminUserResponse {
    private final Long id;
    private final String username;
    private final String email;
    private final String role;
    private final boolean blocked;
    private final String createdAt;

    /**
     * Konstruktor tworzący odpowiedź z danymi użytkownika dla administratora.
     * @param id identyfikator użytkownika
     * @param username nazwa użytkownika
     * @param email adres e-mail
     * @param role rola (np. ADMIN, USER)
     * @param blocked czy konto jest zablokowane
     * @param createdAt data utworzenia konta
     */
    public AdminUserResponse(Long id, String username, String email, String role, boolean blocked, String createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.blocked = blocked;
        this.createdAt = createdAt;
    }

    /**
     * Zwraca identyfikator użytkownika.
     * @return ID użytkownika
     */
    public Long getId() { return id; }

    /**
     * Zwraca nazwę użytkownika.
     * @return nazwa użytkownika
     */
    public String getUsername() { return username; }

    /**
     * Zwraca adres e-mail.
     * @return e-mail
     */
    public String getEmail() { return email; }

    /**
     * Zwraca rolę użytkownika.
     * @return rola
     */
    public String getRole() { return role; }

    /**
     * Sprawdza, czy konto jest zablokowane.
     * @return true, jeśli konto jest zablokowane, false w przeciwnym razie
     */
    public boolean isBlocked() { return blocked; }

    /**
     * Zwraca datę utworzenia konta.
     * @return data utworzenia
     */
    public String getCreatedAt() { return createdAt; }
}
