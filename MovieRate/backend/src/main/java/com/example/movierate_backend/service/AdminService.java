package com.example.movierate_backend.service;

import com.example.movierate_backend.dto.*;
import com.example.movierate_backend.model.User;
import com.example.movierate_backend.repository.UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Serwis realizujący logikę biznesową operacji administracyjnych.
 * Zarządza użytkownikami, filmami oraz weryfikacją wpisów (recenzji).
 */
@Service
public class AdminService {

    private final JdbcTemplate jdbcTemplate;
    private final UserRepository userRepository;

    /**
     * Konstruktor dla AdminService.
     * @param jdbcTemplate interfejs ułatwiający połączenie i wywoływanie zapytań SQL do bazy danych
     * @param userRepository repozytorium do zarządzania jednostkami użytkownika
     */
    public AdminService(JdbcTemplate jdbcTemplate, UserRepository userRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRepository = userRepository;
    }

    // --- Users ---

    /**
     * Pobiera listę wszystkich użytkowników w systemie z bazy danych.
     * @return lista ze zmapowanymi danymi użytkowników przygotowanymi dla panelu admina
     */
    public List<AdminUserResponse> getAllUsers() {
        String sql = """
                SELECT id, username, email, role, is_blocked,
                       created_at::TEXT AS created_at
                FROM users ORDER BY id ASC
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new AdminUserResponse(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("role"),
                        rs.getBoolean("is_blocked"),
                        rs.getString("created_at")
                ));
    }

    /**
     * Wprowadza zmiany przypisanej roli do użytkownika, weryfikując poprawność jej nazwy.
     * @param userId identyfikator użytkownika do zmiany
     * @param role nowa rola (USER lub ADMIN)
     * @throws IllegalArgumentException gdy przekazana rola jest inna niż USER i ADMIN
     */
    public void updateUserRole(Long userId, String role) {
        if (!role.equals("USER") && !role.equals("ADMIN")) {
            throw new IllegalArgumentException("Nieprawidłowa rola: " + role);
        }
        jdbcTemplate.update("UPDATE users SET role = ? WHERE id = ?", role, userId);
    }

    /**
     * Przełącza status blokady użytkownika (is_blocked). Zablokowani użytkownicy mogą stracić dostęp do systemu.
     * @param userId identyfikator blokowanego/odblokowywanego konta
     */
    public void toggleUserBlock(Long userId) {
        jdbcTemplate.update("UPDATE users SET is_blocked = NOT is_blocked WHERE id = ?", userId);
    }

    /**
     * Permanentnie kasuje użytkownika na poziomie bazy danych.
     * @param userId identyfikator konta przeznaczonego do usunięcia
     */
    public void deleteUser(Long userId) {
        jdbcTemplate.update("DELETE FROM users WHERE id = ?", userId);
    }

    // --- Movies ---

    /**
     * Obsługuje proces stworzenia nowego wpisu filmu na podstawie przesłanego żądania i wstawia go do bazy.
     * @param request parametry nowego filmu (tytuł, opis, rok wydania, typ)
     */
    public void createMovie(CreateMovieRequest request) {
        jdbcTemplate.update(
                "INSERT INTO movies (title, description, release_year, type) VALUES (?, ?, ?, ?)",
                request.getTitle(), request.getDescription(), request.getReleaseYear(), request.getType()
        );
    }

    /**
     * Zastępuje stare dane filmowe na nowo przesłane.
     * @param movieId identyfikator zmienianego filmu
     * @param request zaktualizowany zbiór informacji
     */
    public void updateMovie(Long movieId, CreateMovieRequest request) {
        jdbcTemplate.update(
                "UPDATE movies SET title = ?, description = ?, release_year = ?, type = ? WHERE id = ?",
                request.getTitle(), request.getDescription(), request.getReleaseYear(), request.getType(), movieId
        );
    }

    /**
     * Zleca skasowanie produkcji filmowej z bazy danych.
     * @param movieId unikatowy numer id usuwanego filmu
     */
    public void deleteMovie(Long movieId) {
        jdbcTemplate.update("DELETE FROM movies WHERE id = ?", movieId);
    }

    // --- Reviews ---

    /**
     * Pobiera publiczną listę recenzji wraz ze szczegółowymi informacjami, złączając tabele użytkowników i filmów.
     * Zapytanie jest sortowane od najnowszych wpisów.
     * @return zmapowana lista recenzji
     */
    public List<ReviewResponse> getAllReviews() {
        String sql = """
                SELECT rv.id, u.username, u.id AS user_id, m.title AS movie_title,
                       rv.content, rv.created_at::TEXT AS created_at, rv.is_deleted,
                       (SELECT r.rating FROM ratings r WHERE r.user_id = rv.user_id AND r.movie_id = rv.movie_id) AS user_rating,
                       u.profile_picture_url
                FROM reviews rv
                JOIN users u ON u.id = rv.user_id
                JOIN movies m ON m.id = rv.movie_id
                ORDER BY rv.created_at DESC
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new ReviewResponse(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getLong("user_id"),
                        rs.getString("movie_title"),
                        rs.getString("content"),
                        rs.getString("created_at"),
                        rs.getBoolean("is_deleted"),
                        rs.getObject("user_rating", Integer.class),
                        rs.getString("profile_picture_url")
                ));
    }

    /**
     * Administracyjnie zmienia status recenzji na "nieusuniętą" (przydatne przy przywracaniu omyłkowo wykasowanych tekstów).
     * @param reviewId id wybranej recenzji
     */
    public void approveReview(Long reviewId) {
        jdbcTemplate.update("UPDATE reviews SET is_deleted = FALSE WHERE id = ?", reviewId);
    }

    /**
     * Zmienia flagę wybranej recenzji na "usuniętą" (is_deleted = TRUE).
     * Służy moderacji niestosownych recenzji przez pracownika.
     * @param reviewId id wybranej recenzji
     */
    public void deleteReview(Long reviewId) {
        jdbcTemplate.update("UPDATE reviews SET is_deleted = TRUE WHERE id = ?", reviewId);
    }
}
