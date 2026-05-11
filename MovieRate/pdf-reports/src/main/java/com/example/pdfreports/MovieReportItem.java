package com.example.pdfreports;

public record MovieReportItem(
        String title,
        Integer releaseYear,
        String type,
        Double averageRating
) {
}
