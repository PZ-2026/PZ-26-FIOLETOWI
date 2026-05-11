package com.example.movierate_backend.dto;

public record MovieReportItemRequest(
        String title,
        Integer releaseYear,
        String type,
        Double averageRating
) {
}
