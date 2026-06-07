package com.example.movierate_backend.dto;

/**
 * Reprezentuje odpowiedź z danymi dotyczącymi aktywności użytkownika.
 */
public class ActivityResponse {
    private final String type;
    private final String movieTitle;
    private final String detail;
    private final String date;

    /**
     * Konstruktor tworzący obiekt odpowiedzi aktywności.
     * @param type typ aktywności (np. ocenienie, dodanie do ulubionych)
     * @param movieTitle tytuł filmu, którego dotyczy aktywność
     * @param detail szczegóły aktywności (np. wystawiona ocena)
     * @param date data wykonania aktywności
     */
    public ActivityResponse(String type, String movieTitle, String detail, String date) {
        this.type = type;
        this.movieTitle = movieTitle;
        this.detail = detail;
        this.date = date;
    }

    /**
     * Zwraca typ aktywności.
     * @return typ aktywności
     */
    public String getType() { return type; }

    /**
     * Zwraca tytuł filmu.
     * @return tytuł filmu
     */
    public String getMovieTitle() { return movieTitle; }

    /**
     * Zwraca szczegóły aktywności.
     * @return szczegóły aktywności
     */
    public String getDetail() { return detail; }

    /**
     * Zwraca datę wykonania aktywności.
     * @return data wykonania aktywności
     */
    public String getDate() { return date; }
}
