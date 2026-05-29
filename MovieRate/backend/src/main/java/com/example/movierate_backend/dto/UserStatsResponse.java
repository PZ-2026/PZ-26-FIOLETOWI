package com.example.movierate_backend.dto;

public class UserStatsResponse {
    private final long watchedCount;
    private final long ratingCount;
    private final long reviewCount;
    private final long listCount;

    public UserStatsResponse(long watchedCount, long ratingCount, long reviewCount, long listCount) {
        this.watchedCount = watchedCount;
        this.ratingCount = ratingCount;
        this.reviewCount = reviewCount;
        this.listCount = listCount;
    }

    public long getWatchedCount() { return watchedCount; }
    public long getRatingCount() { return ratingCount; }
    public long getReviewCount() { return reviewCount; }
    public long getListCount() { return listCount; }
}
