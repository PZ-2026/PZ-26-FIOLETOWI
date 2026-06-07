package com.example.movierate_backend.dto;

/**
 * Kompletna zwrotka pobrana po żądaniu wyrenderowania wpisu w formie recenzji w serwisie (np w dziale filmu).
 */
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

    /**
     * Podstawowy konstruktor uzupełniający poszczególne parametry zwrotne obiektu ułożone zapytaniem SQL.
     * @param id identyfikator tejże recenzji
     * @param username podpis autora pod recenzją
     * @param userId wewnętrzne id profilu użytkownika zgłaszającego wpis
     * @param movieTitle nawiązanie do recenzowanego obrazu
     * @param content faktyczna tekstowa treść merytoryczna 
     * @param createdAt formatowana data
     * @param deleted informacja z moderacji (is_deleted)
     * @param userRating subiektywna wartość oceniona
     * @param profilePictureUrl ułatwienie budowy UI pod zdjęcia avatarów
     */
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

    /**
     * Zwraca identyfikator.
     * @return ID recenzji
     */
    public Long getId() { return id; }
    
    /**
     * Zwraca nazwę autora.
     * @return nick użytkownika
     */
    public String getUsername() { return username; }
    
    /**
     * Zwraca numeryczne odwołanie do konta użytkownika.
     * @return identyfikator twórcy
     */
    public Long getUserId() { return userId; }
    
    /**
     * Zwraca przypisany tytuł produkcji.
     * @return tytuł
     */
    public String getMovieTitle() { return movieTitle; }
    
    /**
     * Zwraca wypowiedź twórcy.
     * @return kontent
     */
    public String getContent() { return content; }
    
    /**
     * Zwraca datę oddania opinii.
     * @return data utworzenia
     */
    public String getCreatedAt() { return createdAt; }
    
    /**
     * Odczyt flagi systemowej ukrycia komentarza przed publiką.
     * @return parametr czy jest skasowana
     */
    public boolean isDeleted() { return deleted; }
    
    /**
     * Zwraca towarzyszącą wpisowi notę numeryczną.
     * @return ocena w skali dziesiętnej
     */
    public Integer getUserRating() { return userRating; }
    
    /**
     * Pobiera dołączony z zapytania plik graficzny awataru.
     * @return obraz konta
     */
    public String getProfilePictureUrl() { return profilePictureUrl; }
}
