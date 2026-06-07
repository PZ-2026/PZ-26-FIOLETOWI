package com.example.movierate_backend.dto;

/**
 * Reprezentuje pojedynczą pozycję filmową w kontekście raportu.
 * 
 * @param title tytuł filmu
 * @param releaseYear rok wydania filmu
 * @param type typ produkcji (np. film, serial)
 * @param averageRating średnia ocena w systemie
 * @param userRating indywidualna ocena użytkownika
 * @param reviewContent treść ewentualnej recenzji użytkownika
 */
public record MovieReportItemRequest(
        String title,
        Integer releaseYear,
        String type,
        Double averageRating,
        Integer userRating,
        String reviewContent
) {
}
