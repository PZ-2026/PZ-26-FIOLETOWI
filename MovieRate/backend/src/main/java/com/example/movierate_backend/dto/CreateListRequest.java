package com.example.movierate_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Reprezentuje żądanie utworzenia nowej listy użytkownika.
 */
public class CreateListRequest {
    @NotNull(message = "ID użytkownika jest wymagane")
    private Long userId;

    @NotBlank(message = "Nazwa listy jest wymagana")
    private String name;

    @NotBlank(message = "Typ listy jest wymagany")
    private String type;

    /**
     * Zwraca identyfikator użytkownika.
     * @return ID użytkownika
     */
    public Long getUserId() { return userId; }
    
    /**
     * Ustawia identyfikator użytkownika.
     * @param userId nowe ID użytkownika
     */
    public void setUserId(Long userId) { this.userId = userId; }
    
    /**
     * Zwraca nazwę tworzonej listy.
     * @return nazwa listy
     */
    public String getName() { return name; }
    
    /**
     * Ustawia nazwę tworzonej listy.
     * @param name nazwa listy
     */
    public void setName(String name) { this.name = name; }
    
    /**
     * Zwraca typ listy (np. ulubione, do obejrzenia).
     * @return typ listy
     */
    public String getType() { return type; }
    
    /**
     * Ustawia typ listy.
     * @param type nowy typ listy
     */
    public void setType(String type) { this.type = type; }
}
