package com.example.movierate_backend.service;

import com.example.movierate_backend.dto.*;
import com.example.movierate_backend.model.User;
import com.example.movierate_backend.repository.UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Serwis statystyczno-zarządzający ukierunkowany na wyciągnięcie specyficznych informacji
 * o użyciu i działaniach zalogowanej osoby.
 */
@Service
public class UserService {

    private final JdbcTemplate jdbcTemplate;
    private final UserRepository userRepository;

    /**
     * Podstawowy konstruktor warstwy serwisowej profilu.
     * @param jdbcTemplate rdzeń SQL używany w większości statystycznych agregatów
     * @param userRepository standardowe repozytorium użytkowników (Spring Data)
     */
    public UserService(JdbcTemplate jdbcTemplate, UserRepository userRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRepository = userRepository;
    }

    /**
     * Pozwala ocenić stopień "zaangażowania" danego użytkownika zliczając ilości z poszczególnych modułów.
     * Zwraca m.in. licznik obejrzanych, ocenionych filmów czy utworzonych recenzji publicznych.
     * @param userId poszukiwany członek serwisu
     * @return zgromadzony blok danych liczbowych UserStatsResponse
     */
    public UserStatsResponse getUserStats(Long userId) {
        Long watchedCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM user_list_items uli " +
                "JOIN user_lists ul ON ul.id = uli.list_id " +
                "WHERE ul.user_id = ? AND ul.type = 'WATCHED'",
                Long.class, userId
        );

        Long ratingCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM ratings WHERE user_id = ?",
                Long.class, userId
        );

        Long reviewCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM reviews WHERE user_id = ? AND is_deleted = FALSE",
                Long.class, userId
        );

        Long listCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM user_lists WHERE user_id = ?",
                Long.class, userId
        );

        return new UserStatsResponse(
                watchedCount != null ? watchedCount : 0,
                ratingCount != null ? ratingCount : 0,
                reviewCount != null ? reviewCount : 0,
                listCount != null ? listCount : 0
        );
    }

    /**
     * Przeczesuje powiązane oceny i wyłuskuje powtarzające się najczęściej gatunki filmowe
     * przez co ustala pewien gust użytkownika.
     * @param userId unikatowy id profilu
     * @return zliczająca lista powtarzalności dla różnych klas gatunkowych z malejącym licznikiem (od najbardziej lubianych)
     */
    public List<GenreStatResponse> getUserGenres(Long userId) {
        String sql = """
                SELECT g.name, COUNT(*) AS count
                FROM genres g
                JOIN movie_genres mg ON mg.genre_id = g.id
                JOIN ratings r ON r.movie_id = mg.movie_id
                WHERE r.user_id = ?
                GROUP BY g.name
                ORDER BY count DESC
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new GenreStatResponse(rs.getString("name"), rs.getLong("count")), userId);
    }

    /**
     * Tworzy wielozłączną tablicę (UNION) symulującą tablicę ogłoszeniową / oś czasu.
     * Pokazuje chronologicznie ostatnie zachowania (np. dodanie filmu do list, wystawienie opinii, ocenienie w skali 1-10).
     * @param userId analizowany podmiot
     * @return do maksymalnie 10 ostatnich rekordów zdarzeń posortowanych po uniwersalnej dacie z przypisanym typem działania
     */
    public List<ActivityResponse> getUserActivity(Long userId) {
        String sql = """
                (SELECT 'rating' AS type, m.title AS movie_title,
                        CAST(r.rating AS VARCHAR) AS detail, r.created_at::TEXT AS date
                 FROM ratings r JOIN movies m ON m.id = r.movie_id WHERE r.user_id = ?)
                UNION ALL
                (SELECT 'review' AS type, m.title AS movie_title,
                        LEFT(rv.content, 50) AS detail, rv.created_at::TEXT AS date
                 FROM reviews rv JOIN movies m ON m.id = rv.movie_id
                 WHERE rv.user_id = ? AND rv.is_deleted = FALSE)
                UNION ALL
                (SELECT 'list_add' AS type, m.title AS movie_title,
                        ul.name AS detail, m.created_at::TEXT AS date
                 FROM user_list_items uli
                 JOIN movies m ON m.id = uli.movie_id
                 JOIN user_lists ul ON ul.id = uli.list_id
                 WHERE ul.user_id = ?)
                ORDER BY date DESC LIMIT 10
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new ActivityResponse(
                        rs.getString("type"),
                        rs.getString("movie_title"),
                        rs.getString("detail"),
                        rs.getString("date")
                ), userId, userId, userId);
    }

    /**
     * Przeprowadza manualny proces edycji profilu na obiektach i weryfikuje ich istnienie.
     * @param userId użytkownik nadający akcję
     * @param request zbiór parametrów DTO dla prostej zmiany m.in. loginu
     * @return zaktualizowany i zapisany użytkownik w repozytorium JPA
     */
    public User updateProfile(Long userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Użytkownik nie znaleziony"));

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        return userRepository.save(user);
    }
}
