package com.example.movierate_backend.dto;

import java.util.List;

/**
 * Reprezentuje szczegółowe dane filmu zwracane w odpowiedziach API.
 */
public class MovieResponse {
    private final Long id;
    private final String title;
    private final String description;
    private final Integer releaseYear;
    private final String type;
    private final Double averageRating;
    private final List<String> genres;
    private final String imageUrl;

    /**
     * Konstruktor z kompletem parametrów.
     * @param id identyfikator filmu
     * @param title tytuł filmu
     * @param description opis filmu
     * @param releaseYear rok wydania
     * @param type typ (np. film/serial)
     * @param averageRating średnia z ocen użytkowników
     * @param genres lista przypisanych gatunków
     * @param imageUrl adres URL do plakatu filmu
     */
    public MovieResponse(Long id, String title, String description, Integer releaseYear, String type, Double averageRating, List<String> genres, String imageUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.releaseYear = releaseYear;
        this.type = type;
        this.averageRating = averageRating;
        this.genres = genres;
        this.imageUrl = imageUrl;
    }

    /**
     * Pobiera identyfikator filmu.
     * @return id filmu
     */
    public Long getId() {
        return id;
    }

    /**
     * Pobiera tytuł filmu.
     * @return tytuł
     */
    public String getTitle() {
        return title;
    }

    /**
     * Pobiera opis filmu.
     * @return opis
     */
    public String getDescription() {
        return description;
    }

    /**
     * Pobiera rok wydania.
     * @return rok wydania
     */
    public Integer getReleaseYear() {
        return releaseYear;
    }

    /**
     * Pobiera typ produkcji.
     * @return typ
     */
    public String getType() {
        return type;
    }

    /**
     * Pobiera uśrednioną ocenę.
     * @return średnia ocena
     */
    public Double getAverageRating() {
        return averageRating;
    }

    /**
     * Pobiera przypisane gatunki.
     * @return lista nazw gatunków
     */
    public List<String> getGenres() {
        return genres;
    }

    /**
     * Pobiera adres do obrazu/plakatu.
     * @return URL plakatu
     */
    public String getImageUrl() {
        return imageUrl;
    }
}
