package com.example.movierate_backend.dto;

public class UserListItemResponse {
    private final Long id;
    private final Long movieId;
    private final String movieTitle;
    private final Integer releaseYear;
    private final String type;
    private final Double averageRating;
    private final Integer position;

    public UserListItemResponse(Long id, Long movieId, String movieTitle, Integer releaseYear,
                                String type, Double averageRating, Integer position) {
        this.id = id;
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.releaseYear = releaseYear;
        this.type = type;
        this.averageRating = averageRating;
        this.position = position;
    }

    public Long getId() { return id; }
    public Long getMovieId() { return movieId; }
    public String getMovieTitle() { return movieTitle; }
    public Integer getReleaseYear() { return releaseYear; }
    public String getType() { return type; }
    public Double getAverageRating() { return averageRating; }
    public Integer getPosition() { return position; }
}
