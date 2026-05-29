package com.example.movierate_backend.dto;

public class ActivityResponse {
    private final String type;
    private final String movieTitle;
    private final String detail;
    private final String date;

    public ActivityResponse(String type, String movieTitle, String detail, String date) {
        this.type = type;
        this.movieTitle = movieTitle;
        this.detail = detail;
        this.date = date;
    }

    public String getType() { return type; }
    public String getMovieTitle() { return movieTitle; }
    public String getDetail() { return detail; }
    public String getDate() { return date; }
}
