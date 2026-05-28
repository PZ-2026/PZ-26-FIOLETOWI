package com.example.movierate_backend.dto;

import jakarta.validation.constraints.NotNull;

public class AddListItemRequest {
    @NotNull(message = "ID filmu jest wymagane")
    private Long movieId;

    private Integer position;

    public Long getMovieId() { return movieId; }
    public void setMovieId(Long movieId) { this.movieId = movieId; }
    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }
}
