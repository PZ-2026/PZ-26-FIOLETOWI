package com.example.movierate_backend.dto;

/**
 * Reprezentuje odpowiedź zawierającą dane o ocenach.
 */
public class RatingResponse {
    private double averageRating;
    private Integer userRating;

    /**
     * Domyślny konstruktor dla RatingResponse.
     */
    public RatingResponse() {}

    /**
     * Konstruktor z parametrami inicjalizującymi.
     * @param averageRating średnia ocena produkcji
     * @param userRating ocena przydzielona przez konkretnego użytkownika
     */
    public RatingResponse(double averageRating, Integer userRating) {
        this.averageRating = averageRating;
        this.userRating = userRating;
    }

    /**
     * Pobiera średnią ocenę.
     * @return średnia ocena
     */
    public double getAverageRating() {
        return averageRating;
    }

    /**
     * Ustawia średnią ocenę.
     * @param averageRating średnia ocena
     */
    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    /**
     * Pobiera ocenę użytkownika.
     * @return ocena użytkownika lub null, jeśli nie oceniono
     */
    public Integer getUserRating() {
        return userRating;
    }

    /**
     * Ustawia ocenę użytkownika.
     * @param userRating nowa ocena użytkownika
     */
    public void setUserRating(Integer userRating) {
        this.userRating = userRating;
    }
}
