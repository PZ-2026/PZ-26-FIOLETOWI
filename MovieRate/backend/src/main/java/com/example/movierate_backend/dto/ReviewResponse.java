package com.example.movierate_backend.dto;

public class ReviewResponse {
    private final Long id;
    private final String username;
    private final Long userId;
    private final String movieTitle;
    private final String content;
    private final String createdAt;
    private final boolean deleted;
    private final Integer userRating;
    private final String profilePictureUrl;

    public ReviewResponse(Long id, String username, Long userId, String movieTitle, String content, String createdAt, boolean deleted, Integer userRating, String profilePictureUrl) {
        this.id = id;
        this.username = username;
        this.userId = userId;
        this.movieTitle = movieTitle;
        this.content = content;
        this.createdAt = createdAt;
        this.deleted = deleted;
        this.userRating = userRating;
        this.profilePictureUrl = profilePictureUrl;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public Long getUserId() { return userId; }
    public String getMovieTitle() { return movieTitle; }
    public String getContent() { return content; }
    public String getCreatedAt() { return createdAt; }
    public boolean isDeleted() { return deleted; }
    public Integer getUserRating() { return userRating; }
    public String getProfilePictureUrl() { return profilePictureUrl; }
}
