package com.example.movierate_backend.dto;

public class RatingResponse {
    private double averageRating;
    private Integer userRating;

    public RatingResponse() {}

    public RatingResponse(double averageRating, Integer userRating) {
        this.averageRating = averageRating;
        this.userRating = userRating;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public Integer getUserRating() {
        return userRating;
    }

    public void setUserRating(Integer userRating) {
        this.userRating = userRating;
    }
}
