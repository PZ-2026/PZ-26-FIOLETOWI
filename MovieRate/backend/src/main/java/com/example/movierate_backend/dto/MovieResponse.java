package com.example.movierate_backend.dto;

public class MovieResponse {
    private final Long id;
    private final String title;
    private final String description;
    private final Integer releaseYear;
    private final String type;
    private final Double averageRating;

    public MovieResponse(Long id, String title, String description, Integer releaseYear, String type, Double averageRating) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.releaseYear = releaseYear;
        this.type = type;
        this.averageRating = averageRating;
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
}
