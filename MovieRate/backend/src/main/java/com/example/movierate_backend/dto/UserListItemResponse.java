package com.example.movierate_backend.dto;

/**
 * Pojedyncza pozycja przypisana do utworzonej uprzednio listy, posiadająca skrócone dane o tytule dzieła.
 */
public class UserListItemResponse {
    private final Long id;
    private final Long movieId;
    private final String movieTitle;
    private final Integer releaseYear;
    private final String type;
    private final Double averageRating;
    private final Integer position;
    private final String imageUrl;

    /**
     * Mapujący konstruktor wyników zapytania bazodanowego pod listę użytkownika.
     * @param id odnośnik do encji powiązania (item-id)
     * @param movieId id zapisanego obrazu
     * @param movieTitle tytuł
     * @param releaseYear rocznik publikacji
     * @param type rodzaj medium
     * @param averageRating ogólnospołeczna nota
     * @param position hierarchia wpisu na wytypowanej liście
     * @param imageUrl ścieżka do plakatu w galerii
     */
    public UserListItemResponse(Long id, Long movieId, String movieTitle, Integer releaseYear,
                                String type, Double averageRating, Integer position, String imageUrl) {
        this.id = id;
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.releaseYear = releaseYear;
        this.type = type;
        this.averageRating = averageRating;
        this.position = position;
        this.imageUrl = imageUrl;
    }

    /**
     * Zwraca identyfikator wpisu powiązania dla listy.
     * @return ID elementu
     */
    public Long getId() { return id; }
    
    /**
     * Zwraca odwołanie do zapisanego tytułu.
     * @return numer rekordu powiązanego dzieła
     */
    public Long getMovieId() { return movieId; }
    
    /**
     * Pobiera dołączoną nazwę/tytuł filmu.
     * @return tytuł powiązania
     */
    public String getMovieTitle() { return movieTitle; }
    
    /**
     * Pobiera rocznik wybranego z listy dzieła.
     * @return rocznik premiery 
     */
    public Integer getReleaseYear() { return releaseYear; }
    
    /**
     * Zwraca określony format dzieła np. seria telewizyjna, film kinowy.
     * @return kategoria wydawnicza
     */
    public String getType() { return type; }
    
    /**
     * Zwraca wyliczoną w serwisie ocenę z oddanych głosów.
     * @return uśredniona wartość globalna
     */
    public Double getAverageRating() { return averageRating; }
    
    /**
     * Zwraca opcjonalne pozycjonowanie na liście.
     * @return indeks przypisany rekordu
     */
    public Integer getPosition() { return position; }
    
    /**
     * Pobiera w pełni określony link pod miniaturkę obrazującą dzieło z okładki.
     * @return odnośnik http grafiki
     */
    public String getImageUrl() { return imageUrl; }
}
