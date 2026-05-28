package com.example.movierate_backend.dto;

import java.util.List;

public class MovieResponse {
    private final Long id;
    private final String title;
    private final String description;
    private final Integer releaseYear;
    private final String type;
    private final Double averageRating;
    private final List<String> genres;
    private final String imageUrl;

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

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public String getType() {
        return type;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public List<String> getGenres() {
        return genres;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
