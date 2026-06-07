package com.example.movierate_backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

/**
 * Reprezentuje żądanie wygenerowania raportu dla filmów.
 * 
 * @param title tytuł raportu
 * @param generatedBy informacja o osobie generującej raport
 * @param movies lista pozycji filmowych wchodzących w skład raportu
 */
public record MovieReportRequest(
        @NotBlank String title,
        String generatedBy,
        @Valid List<MovieReportItemRequest> movies
) {
}
