package com.example.movierate_backend.dto;

/**
 * Reprezentuje statystyki konkretnego gatunku filmowego powiązanego z użytkownikiem.
 */
public class GenreStatResponse {
    private final String name;
    private final long count;

    /**
     * Konstruktor inicjalizujący statystykę gatunku.
     * @param name nazwa gatunku
     * @param count liczba wystąpień/ocen dla danego gatunku
     */
    public GenreStatResponse(String name, long count) {
        this.name = name;
        this.count = count;
    }

    /**
     * Pobiera nazwę gatunku.
     * @return nazwa gatunku
     */
    public String getName() { return name; }

    /**
     * Pobiera liczbę przypisań tego gatunku.
     * @return liczba wystąpień
     */
    public long getCount() { return count; }
}
