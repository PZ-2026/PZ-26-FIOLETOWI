package com.example.movierate_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Żądanie dodania lub edycji wpisu o filmie.
 */
public class CreateMovieRequest {
    @NotBlank(message = "Tytuł jest wymagany")
    private String title;

    private String description;

    @NotNull(message = "Rok produkcji jest wymagany")
    private Integer releaseYear;

    @NotBlank(message = "Typ jest wymagany")
    private String type;

    private List<Long> genreIds;

    private String imageUrl;

    private List<String> actorNames;

    /**
     * Zwraca tytuł produkcji.
     * @return tytuł
     */
    public String getTitle() { return title; }
    
    /**
     * Ustawia tytuł.
     * @param title nazwa produkcji
     */
    public void setTitle(String title) { this.title = title; }
    
    /**
     * Zwraca opis fabuły.
     * @return opis produkcji
     */
    public String getDescription() { return description; }
    
    /**
     * Ustawia opis.
     * @param description opis filmu
     */
    public void setDescription(String description) { this.description = description; }
    
    /**
     * Zwraca rocznik premiery.
     * @return rok
     */
    public Integer getReleaseYear() { return releaseYear; }
    
    /**
     * Ustawia rocznik.
     * @param releaseYear nowy rok wydania
     */
    public void setReleaseYear(Integer releaseYear) { this.releaseYear = releaseYear; }
    
    /**
     * Zwraca rodzaj produkcji (np. serial, dokument).
     * @return typ
     */
    public String getType() { return type; }
    
    /**
     * Ustawia rodzaj produkcji.
     * @param type typ produkcji
     */
    public void setType(String type) { this.type = type; }
    
    /**
     * Zwraca identyfikatory wybranych gatunków dla filmu.
     * @return lista ID
     */
    public List<Long> getGenreIds() { return genreIds; }
    
    /**
     * Ustawia id gatunków.
     * @param genreIds nowa lista tagów ID gatunków
     */
    public void setGenreIds(List<Long> genreIds) { this.genreIds = genreIds; }
    
    /**
     * Zwraca odnośnik sieciowy do zdjęcia filmu.
     * @return adres HTTP
     */
    public String getImageUrl() { return imageUrl; }
    
    /**
     * Ustawia adres pliku graficznego reprezentującego dzieło.
     * @param imageUrl nowy adres
     */
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    /**
     * Zwraca listę aktorów powiązanych z filmem.
     * @return lista nazw aktorów
     */
    public List<String> getActorNames() { return actorNames; }

    /**
     * Ustawia listę aktorów powiązanych z filmem.
     * @param actorNames lista nazw aktorów
     */
    public void setActorNames(List<String> actorNames) { this.actorNames = actorNames; }
}
