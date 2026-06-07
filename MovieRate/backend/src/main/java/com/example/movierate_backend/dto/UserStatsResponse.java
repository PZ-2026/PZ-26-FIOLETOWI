package com.example.movierate_backend.dto;

/**
 * Podsumowujący DTO generujący statystyki aktywności odzwierciedlone jako metryki m.in na głównej planszy dashboardu profilowego.
 */
public class UserStatsResponse {
    private final long watchedCount;
    private final long ratingCount;
    private final long reviewCount;
    private final long listCount;

    /**
     * Narzuca na klasę wyciągnięte statystyki.
     * @param watchedCount policzone sumarycznie zaobserwowane filmy
     * @param ratingCount ilość przyporządkowanych ocen (not)
     * @param reviewCount wolumen wypowiedzi zrecenzowanych
     * @param listCount zrealizowana liczba folderów tematycznych z bazą
     */
    public UserStatsResponse(long watchedCount, long ratingCount, long reviewCount, long listCount) {
        this.watchedCount = watchedCount;
        this.ratingCount = ratingCount;
        this.reviewCount = reviewCount;
        this.listCount = listCount;
    }

    /**
     * Pobiera statystykę obejrzanych sztuk filmów (historycznie ocenionych lub dodanych w sekcję 'Widziane').
     * @return sumarycznie naliczony ciąg dzieł
     */
    public long getWatchedCount() { return watchedCount; }
    
    /**
     * Odczytuje wolumen skategoryzowanych opinii liczbowych przypisanych od recenzenta.
     * @return miara wszystkich zadeklarowanych przez niego not
     */
    public long getRatingCount() { return ratingCount; }
    
    /**
     * Zwraca wartość przeliczonych komentarzy.
     * @return liczba zatwierdzonych tekstowych form wypowiedzi gracza
     */
    public long getReviewCount() { return reviewCount; }
    
    /**
     * Daje namiar na rozmach katalogowania biblioteki przez odnośnego członka zespołu.
     * @return wypracowana kolekcja utworzonych zbiorów
     */
    public long getListCount() { return listCount; }
}
