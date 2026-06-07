package com.example.movierate_backend.dto;

import jakarta.validation.constraints.NotNull;

/**
 * Żądanie dodania filmu do listy.
 */
public class AddListItemRequest {
    @NotNull(message = "ID filmu jest wymagane")
    private Long movieId;

    private Integer position;

    /**
     * Zwraca ID filmu.
     * @return identyfikator filmu
     */
    public Long getMovieId() { return movieId; }

    /**
     * Ustawia ID filmu.
     * @param movieId identyfikator filmu
     */
    public void setMovieId(Long movieId) { this.movieId = movieId; }

    /**
     * Zwraca pozycję filmu na liście.
     * @return pozycja
     */
    public Integer getPosition() { return position; }

    /**
     * Ustawia pozycję filmu na liście.
     * @param position pozycja
     */
    public void setPosition(Integer position) { this.position = position; }
}
