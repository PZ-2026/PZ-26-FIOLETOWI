package com.example.movierate_backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record MovieReportRequest(
        @NotBlank String title,
        String generatedBy,
        @Valid List<MovieReportItemRequest> movies
) {
}
