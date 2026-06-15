package com.example.movierate_backend.service;

import com.example.movierate_backend.dto.CastMemberResponse;
import com.example.movierate_backend.dto.MovieReportItemRequest;
import com.example.movierate_backend.dto.MovieResponse;
import com.example.movierate_backend.dto.RatingResponse;
import com.example.movierate_backend.dto.ReviewResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Złożony serwis zarządzający logiką domenową sekcji filmów.
 * Pobiera dane filmowe, statystyki, powiązania gatunków oraz obsługuje proces wystawiania ocen i recenzji.
 */
@Service
public class MovieService {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Konstruktor z wstrzyknięciem JDBC.
     * @param jdbcTemplate rdzeń Spring do manipulowania surowym SQL
     */
    public MovieService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Zbiera posortowaną listę twórców wybranego filmu uwzględniając podział na role.
     * @param movieId identyfikator filmu
     * @return zmapowani członkowie obsady w kolejności od reżysera przez aktorów
     */
    public List<CastMemberResponse> getMovieCast(Long movieId) {
        String sql = """
                SELECT p.name, r.name AS role
                FROM movie_cast mc
                JOIN people p ON p.id = mc.person_id
                JOIN roles r ON r.id = mc.role_id
                WHERE mc.movie_id = ?
                ORDER BY
                    CASE r.name
                        WHEN 'DIRECTOR' THEN 1
                        WHEN 'WRITER' THEN 2
                        WHEN 'ACTOR' THEN 3
                        ELSE 4
                    END,
                    p.name ASC
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> new CastMemberResponse(
                rs.getString("name"),
                rs.getString("role")
        ), movieId);
    }

    /**
     * Wyciąga aktualny wskaźnik i zsumowaną uśrednioną ocenę danego filmu, jak i ocenę konkretnego usera jeśli ją wystawił.
     * @param movieId identyfikator sprawdzanego filmu
     * @param userId konto przypisane do zapytania
     * @return zbiorczy obiekt wskaźników ocen filmu RatingResponse
     */
    public RatingResponse getUserRating(Long movieId, Long userId) {
        // Get average rating
        Double avgRating = jdbcTemplate.query(
            "SELECT COALESCE(ROUND(AVG(rating)::numeric, 1), 0) FROM ratings WHERE movie_id = ?",
            (rs) -> rs.next() ? rs.getDouble(1) : 0.0,
            movieId);

        // Get user's rating
        Integer userRating = jdbcTemplate.query(
            "SELECT rating FROM ratings WHERE movie_id = ? AND user_id = ?",
            (rs) -> rs.next() ? rs.getInt("rating") : null,
            movieId, userId);

        return new RatingResponse(avgRating != null ? avgRating : 0.0, userRating);
    }

    /**
     * Obsługuje mechanizm oceniania produkcji ("gwiazdek").
     * Zapobiega błędom i duplikacjom wykorzystując procedurę ON CONFLICT.
     * @param movieId identyfikator docelowy
     * @param userId użytkownik nadający ocenę
     * @param rating wartość liczbowa oceny od 1 do 10
     * @return zaktualizowany wynik wskaźników po wpłynięciu nowej oceny
     * @throws IllegalArgumentException w wypadku wartości oceny spoza skali
     */
    public RatingResponse rateMovie(Long movieId, Long userId, int rating) {
        if (rating < 1 || rating > 10) {
            throw new IllegalArgumentException("Rating must be between 1 and 10");
        }
        jdbcTemplate.update(
            "INSERT INTO ratings (user_id, movie_id, rating) VALUES (?, ?, ?) " +
            "ON CONFLICT (user_id, movie_id) DO UPDATE SET rating = ?",
            userId, movieId, rating, rating);

        return getUserRating(movieId, userId);
    }

    /**
     * Wycofuje (usuwa) decyzję o przyznaniu oceny przez użytkownika.
     * @param movieId identyfikator filmu
     * @param userId konto anulujące
     * @return nowe wskaźniki bez uwzględniania usuniętej wartości
     */
    public RatingResponse deleteRating(Long movieId, Long userId) {
        jdbcTemplate.update(
            "DELETE FROM ratings WHERE user_id = ? AND movie_id = ?",
            userId, movieId);
        return getUserRating(movieId, userId);
    }

    /**
     * Agreguje i formatuje wszystkie filmy dostępne w serwisie.
     * @return kompletna tablica obiektów MovieResponse
     */
    public List<MovieResponse> getAllMovies() {
        return jdbcTemplate.query(baseMovieQuery() + " ORDER BY m.title ASC", this::mapMovie);
    }

    /**
     * Buduje selekcję na podstawie parametru średniej wyciągniętej w bazie danych.
     * @param limit węższe grono np. TOP 10
     * @return zestawienie rankingowe filmów
     */
    public List<MovieResponse> getTopRatedMovies(int limit) {
        return jdbcTemplate.query(
                baseMovieQuery() + " ORDER BY average_rating DESC NULLS LAST, m.title ASC LIMIT ?",
                this::mapMovie,
                limit
        );
    }

    /**
     * Formułuje najświeższe produkcje.
     * @param limit liczba elementów (z reguły np. strona powitalna)
     * @return filmy najmłodsze wiekiem premiery
     */
    public List<MovieResponse> getNewestMovies(int limit) {
        return jdbcTemplate.query(
                baseMovieQuery() + " ORDER BY m.release_year DESC, m.title ASC LIMIT ?",
                this::mapMovie,
                limit
        );
    }

    /**
     * Znajduje filmy spełniające kryterium konkretnej kategorii/gatunku m.in. Action czy Drama.
     * @param genre podana nazwa szukanego gatunku
     * @param limit restrykcja zliczania dla frontendu
     * @return filmy dopasowane na bazie tabel krzyżowych movie_genres
     */
    public List<MovieResponse> getMoviesByGenre(String genre, int limit) {
        String sql = "SELECT * FROM (" + baseMovieQuery() + ") filtered" +
                " WHERE filtered.id IN (" +
                "   SELECT mg.movie_id FROM movie_genres mg" +
                "   JOIN genres g ON g.id = mg.genre_id" +
                "   WHERE LOWER(g.name) = LOWER(?)" +
                ")" +
                " ORDER BY filtered.average_rating DESC NULLS LAST, filtered.title ASC LIMIT ?";
        return jdbcTemplate.query(sql, this::mapMovie, genre, limit);
    }

    /**
     * Podstawowy skrypt wielo-zapytaniowej wyszukiwarki filmów.
     * Elastycznie modyfikuje bazowe zapytanie po wejściowych null/nie-null zmiennych.
     * @param query część nazwy tytułu szukana w stringu (za pomocą LIKE)
     * @param type filtr określający czy film / serial
     * @param year ścisły strzał w rocznik premiery
     * @return lista pasujących kandydatów
     */
    public List<MovieResponse> searchMovies(String query, String type, Integer year) {
        StringBuilder sql = new StringBuilder("SELECT * FROM (");
        sql.append(baseMovieQuery()).append(") filtered");
        sql.append(" WHERE 1=1");

        if (query != null && !query.isBlank()) {
            sql.append(" AND LOWER(filtered.title) LIKE LOWER(?)");
        }
        if (type != null && !type.isBlank()) {
            sql.append(" AND filtered.type = ?");
        }
        if (year != null) {
            sql.append(" AND filtered.release_year = ?");
        }

        sql.append(" ORDER BY filtered.title ASC");

        List<Object> params = new ArrayList<>();
        if (query != null && !query.isBlank()) {
            params.add("%" + query + "%");
        }
        if (type != null && !type.isBlank()) {
            params.add(type);
        }
        if (year != null) {
            params.add(year);
        }

        return jdbcTemplate.query(sql.toString(), this::mapMovie, params.toArray());
    }

    /**
     * Tworzy główną instancję zapytania SELECT, łącząc oceny i konkatenuje gatunki.
     * Narzędzie służące DRY i zmniejszeniu duplikacji zapytań SQL w innych metodach.
     * @return ciąg zapytania
     */
    private String baseMovieQuery() {
        return """
                SELECT
                    m.id,
                    m.title,
                    m.description,
                    m.release_year,
                    m.type,
                    m.image_url,
                    COALESCE(ROUND(AVG(r.rating)::numeric, 1), 0) AS average_rating,
                    COALESCE(
                        (SELECT STRING_AGG(g.name, ',') FROM movie_genres mg2
                         JOIN genres g ON g.id = mg2.genre_id
                         WHERE mg2.movie_id = m.id),
                        ''
                    ) AS genres
                FROM movies m
                LEFT JOIN ratings r ON r.movie_id = m.id
                GROUP BY m.id, m.title, m.description, m.release_year, m.type, m.image_url
                """;
    }

    /**
     * Metoda przetwarzająca surowe wiersze zapytania na znormalizowany standard obiektu MovieResponse DTO.
     */
    private MovieResponse mapMovie(ResultSet rs, int rowNum) throws SQLException {
        String genresStr = rs.getString("genres");
        List<String> genres = (genresStr != null && !genresStr.isBlank())
                ? Arrays.stream(genresStr.split(",")).map(String::trim).collect(Collectors.toList())
                : new ArrayList<>();

        // Use image_url from DB if set, otherwise generate a deterministic one
        String imageUrl = rs.getString("image_url");
        if (imageUrl == null || imageUrl.isBlank()) {
            imageUrl = "https://picsum.photos/seed/movie" + rs.getLong("id") + "/300/450";
        }

        return new MovieResponse(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getInt("release_year"),
                rs.getString("type"),
                rs.getDouble("average_rating"),
                genres,
                imageUrl
        );
    }

    /**
     * Wchodzi w relację z profilem autorów by wyświetlić wszystkie dopuszczone (nie moderowane - is_deleted FALSE)
     * publiczne komentarze i recenzje dla profilu konkretnego dzieła.
     * @param movieId identyfikator recenzowanego obiektu
     * @return lista zebranych opinii
     */
    public List<ReviewResponse> getMovieReviews(Long movieId) {
        String sql = """
                SELECT rv.id, u.username, u.id AS user_id, m.title AS movie_title,
                       rv.content, rv.created_at::TEXT AS created_at, rv.is_deleted,
                       (SELECT r.rating FROM ratings r WHERE r.user_id = rv.user_id AND r.movie_id = rv.movie_id) AS user_rating,
                       u.profile_picture_url
                FROM reviews rv
                JOIN users u ON u.id = rv.user_id
                JOIN movies m ON m.id = rv.movie_id
                WHERE rv.movie_id = ? AND rv.is_deleted = FALSE AND rv.is_approved = TRUE
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
                ), movieId);
    }

    /**
     * Bezpośrednie zapisanie stworzonej opinii użytkownika powiązanej z daną produkcją.
     * @param movieId wpis dla filmu
     * @param userId numer autora wpisu
     * @param content tekstowa, właściwa recenzja dzieła
     */
    public void addReview(Long movieId, Long userId, String content) {
        jdbcTemplate.update(
            "INSERT INTO reviews (user_id, movie_id, content, is_approved) VALUES (?, ?, ?, FALSE)",
            userId, movieId, content);
    }

    /**
     * Wywołuje mechanizm 'soft-delete' flagując opinie niepożądane bądź usuwane przez samego usera
     * na logiczne is_deleted TRUE bez utraty danych archiwalnych w samej bazie.
     * @param reviewId referencja recenzji
     * @param userId zabezpieczenie autorskie w WHERE w celu uniemożliwienia skasowania nie swojej opinii
     */
    public void deleteReview(Long reviewId, Long userId) {
        jdbcTemplate.update(
            "UPDATE reviews SET is_deleted = TRUE WHERE id = ? AND user_id = ?",
            reviewId, userId);
    }

    /**
     * Płynna edycja istniejacej recenzji polegająca na nowym zaktualizowaniu samego ciągu znaków
     * pod warunkiem, że edytujący jest jej właścicielem.
     * @param reviewId recenzja podlegająca modernizacji
     * @param userId wymuszenie by to użytkownik był właścicielem zmian (zastępstwo klasycznej autoryzacji metodyki)
     * @param content nowy blok opinii
     */
    public void updateReview(Long reviewId, Long userId, String content) {
        jdbcTemplate.update(
            "UPDATE reviews SET content = ? WHERE id = ? AND user_id = ? AND is_deleted = FALSE",
            content, reviewId, userId);
    }

    /**
     * Metoda używana główie przez silnik podsumowań i statystyk w ReportService.
     * Zbiera całą gamę ocenionych przez użytkownika dzieł by przenieść to do formy zestawień np. w wygenerowanym PDF.
     * @param userId generujący profil użytkownik
     * @return uproszczone pakiety gotowe do transformacji tabeli
     */
    public List<MovieReportItemRequest> getUserRatedMovies(Long userId) {
        String sql = """
                SELECT m.title, m.release_year, m.type,
                       COALESCE((SELECT AVG(r2.rating)::numeric(10,2) FROM ratings r2 WHERE r2.movie_id = m.id), 0) AS average_rating,
                       r.rating AS user_rating
                FROM ratings r
                JOIN movies m ON m.id = r.movie_id
                WHERE r.user_id = ?
                ORDER BY m.title
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new MovieReportItemRequest(
                        rs.getString("title"),
                        rs.getInt("release_year"),
                        rs.getString("type"),
                        rs.getDouble("average_rating"),
                        rs.getInt("user_rating"),
                        null
                ), userId);
    }

    /**
     * Tożsama funkcja w stosunku do getUserRatedMovies, z tym że skupiająca się na wyciągnięciu napisanej
     * tekstowo recenzji do konkretnego tytułu by umieścić m.in. w archiwum pliku.
     * @param userId badane konto
     * @return model zawierający recenzje z ominięciem tych skasowanych
     */
    public List<MovieReportItemRequest> getUserReviewedMovies(Long userId) {
        String sql = """
                SELECT m.title, m.release_year, m.type,
                       COALESCE((SELECT AVG(r2.rating)::numeric(10,2) FROM ratings r2 WHERE r2.movie_id = m.id), 0) AS average_rating,
                       rv.content AS review_content
                FROM reviews rv
                JOIN movies m ON m.id = rv.movie_id
                WHERE rv.user_id = ? AND rv.is_deleted = FALSE
                ORDER BY m.title
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new MovieReportItemRequest(
                        rs.getString("title"),
                        rs.getInt("release_year"),
                        rs.getString("type"),
                        rs.getDouble("average_rating"),
                        null,
                        rs.getString("review_content")
                ), userId);
    }
}
