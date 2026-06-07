package com.example.movierate_backend.dto;

/**
 * Odpowiedź z danymi członka obsady dla wyświetlenia profilu filmu.
 */
public class CastMemberResponse {
    private final String name;
    private final String role;

    /**
     * Konstruktor z parametrami aktora/ekipy.
     * @param name imię i nazwisko artysty
     * @param role odgrywana postać lub funkcja (np. Reżyser)
     */
    public CastMemberResponse(String name, String role) {
        this.name = name;
        this.role = role;
    }

    /**
     * Pobiera nazwisko.
     * @return imię i nazwisko
     */
    public String getName() {
        return name;
    }

    /**
     * Pobiera odgrywaną rolę.
     * @return rola członka obsady
     */
    public String getRole() {
        return role;
    }
}
