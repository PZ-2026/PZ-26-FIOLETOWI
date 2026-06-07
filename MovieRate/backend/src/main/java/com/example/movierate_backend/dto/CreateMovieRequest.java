package com.example.movierate_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

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

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getReleaseYear() { return releaseYear; }
    public void setReleaseYear(Integer releaseYear) { this.releaseYear = releaseYear; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public List<Long> getGenreIds() { return genreIds; }
    public void setGenreIds(List<Long> genreIds) { this.genreIds = genreIds; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
