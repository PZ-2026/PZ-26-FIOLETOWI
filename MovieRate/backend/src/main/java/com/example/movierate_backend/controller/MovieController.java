package com.example.movierate_backend.controller;

import com.example.movierate_backend.dto.CastMemberResponse;
import com.example.movierate_backend.dto.MovieReportItemRequest;
import com.example.movierate_backend.dto.MovieResponse;
import com.example.movierate_backend.dto.RatingResponse;
import com.example.movierate_backend.dto.ReviewResponse;
import com.example.movierate_backend.service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Kontroler REST obsługujący zapytania dotyczące bazy filmów.
 * Umożliwia wyszukiwanie, ocenianie i recenzowanie pozycji filmowych.
 */
@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    /**
     * Konstruktor wstrzykujący serwis filmowy.
     * @param movieService główny serwis dla warstwy logiki filmów
     */
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    /**
     * Pobiera listę wszystkich dostępnych filmów.
     * @return lista wszystkich obiektów MovieResponse
     */
    @GetMapping
    public List<MovieResponse> getAllMovies() {
        return movieService.getAllMovies();
    }

    /**
     * Zwraca listę najlepiej ocenianych filmów.
     * @param limit opcjonalny parametr limitu zwracanych filmów (domyślnie 10)
     * @return lista najlepiej ocenianych filmów
     */
    @GetMapping("/top-rated")
    public List<MovieResponse> getTopRatedMovies(@RequestParam(defaultValue = "10") int limit) {
        return movieService.getTopRatedMovies(limit);
    }

    /**
     * Zwraca zestawienie najnowszych filmów.
     * @param limit maksymalna ilość zwracanych nowości (domyślnie 10)
     * @return najnowsze filmy z bazy
     */
    @GetMapping("/newest")
    public List<MovieResponse> getNewestMovies(@RequestParam(defaultValue = "10") int limit) {
        return movieService.getNewestMovies(limit);
    }

    /**
     * Pobiera filmy dla wskazanego gatunku.
     * @param genre gatunek filmowy przekazany z parametru zapytania
     * @param limit liczba filmów do zwrócenia (domyślnie 10)
     * @return lista filmów pasujących do konkretnego gatunku
     */
    @GetMapping("/by-genre")
    public List<MovieResponse> getMoviesByGenre(
            @RequestParam String genre,
            @RequestParam(defaultValue = "10") int limit) {
        return movieService.getMoviesByGenre(genre, limit);
    }

    /**
     * Zwraca obsadę występującą w wybranym filmie.
     * @param id identyfikator filmu
     * @return lista powiązanej obsady
     */
    @GetMapping("/{id}/cast")
    public List<CastMemberResponse> getMovieCast(@PathVariable Long id) {
        return movieService.getMovieCast(id);
    }

    /**
     * Moduł prostej wyszukiwarki filmów wspierający filtrowanie po tytule, typie i roku.
     * @param q zapytanie tekstowe/tytuł (np. wpisywane w pole szukania)
     * @param type kategoria filmu (np. MOVIE / SERIES)
     * @param year dokładny rok wydania
     * @return pofiltrowana lista filmów
     */
    @GetMapping("/search")
    public List<MovieResponse> searchMovies(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer year) {
        return movieService.searchMovies(q, type, year);
    }

    /**
     * Pobiera dotychczasową ocenę dodaną dla wybranego filmu przez wskazanego użytkownika.
     * @param id identyfikator filmu
     * @param userId identyfikator użytkownika z parametru zapytania
     * @return reprezentacja oceny lub domyślne parametry
     */
    @GetMapping("/{id}/rating")
    public ResponseEntity<RatingResponse> getUserRating(
            @PathVariable Long id,
            @RequestParam Long userId) {
        return ResponseEntity.ok(movieService.getUserRating(id, userId));
    }

    /**
     * Pozwala użytkownikowi ocenić wskazany film.
     * @param id identyfikator filmu, na którym operujemy
     * @param body zawartość żądania posiadająca identyfikator użytkownika i liczbową ocenę
     * @return nowe lub zaktualizowane parametry oceny filmu w formie RatingResponse
     */
    @PostMapping("/{id}/rate")
    public ResponseEntity<RatingResponse> rateMovie(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body) {
        Long userId = Long.valueOf(body.get("userId").toString());
        int rating = Integer.parseInt(body.get("rating").toString());
        try {
            return ResponseEntity.ok(movieService.rateMovie(id, userId, rating));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Usuwa wybraną ocenę u konkretnego filmu.
     * @param id identyfikator filmu
     * @param userId użytkownik, którego ocena ma zostać usunięta
     * @return zaktualizowane parametry średniej oceny dla filmu
     */
    @DeleteMapping("/{id}/rating")
    public ResponseEntity<RatingResponse> deleteRating(
            @PathVariable Long id,
            @RequestParam Long userId) {
        return ResponseEntity.ok(movieService.deleteRating(id, userId));
    }

    /**
     * Pobiera wszystkie zatwierdzone recenzje przypisane do danego filmu.
     * @param id identyfikator filmu
     * @return publiczna lista recenzji
     */
    @GetMapping("/{id}/reviews")
    public ResponseEntity<List<ReviewResponse>> getMovieReviews(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.getMovieReviews(id));
    }

    /**
     * Dodaje nową recenzję dla wskazanego filmu.
     * @param id identyfikator filmu
     * @param body request posiadający pole id użytkownika oraz tekstowe "content"
     * @return kod 201 Created przy poprawnym utworzeniu recenzji
     */
    @PostMapping("/{id}/reviews")
    public ResponseEntity<Void> addReview(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body) {
        Long userId = Long.valueOf(body.get("userId").toString());
        String content = body.get("content").toString();
        movieService.addReview(id, userId, content);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Usuwa recenzję przypisaną do filmu przez właściciela (tzw. logiczne usunięcie).
     * @param id identyfikator filmu
     * @param reviewId unikatowe id przypisanej wcześniej recenzji
     * @param userId użytkownik zgłaszający usunięcie recenzji
     * @return kod 204 No Content po udanej kasacji
     */
    @DeleteMapping("/{id}/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long id,
            @PathVariable Long reviewId,
            @RequestParam Long userId) {
        movieService.deleteReview(reviewId, userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Wprowadza zmiany w istniejącej recenzji (edycja posta).
     * @param id identyfikator filmu
     * @param reviewId id modyfikowanej recenzji
     * @param body polecenia zmian, m.in. zmieniony tekst
     * @return status 200 OK sygnalizujący zapisanie nowego wpisu
     */
    @PutMapping("/{id}/reviews/{reviewId}")
    public ResponseEntity<Void> updateReview(
            @PathVariable Long id,
            @PathVariable Long reviewId,
            @RequestBody Map<String, Object> body) {
        Long userId = Long.valueOf(body.get("userId").toString());
        String content = body.get("content").toString();
        movieService.updateReview(reviewId, userId, content);
        return ResponseEntity.ok().build();
    }

    /**
     * Pobiera listę filmów, które ocenił dany użytkownik (służy głównie dla systemu raportowego).
     * @param userId unikatowy numer użytkownika systemu
     * @return zestawienie elementów filmowych skojarzonych z ocenami
     */
    @GetMapping("/rated")
    public ResponseEntity<List<MovieReportItemRequest>> getUserRatedMovies(@RequestParam Long userId) {
        return ResponseEntity.ok(movieService.getUserRatedMovies(userId));
    }

    /**
     * Pobiera zestawienie filmów recenzowanych przez użytkownika.
     * @param userId twórca recenzji
     * @return zestawienie filmów poszerzonych o recenzje stworzone przez wskazaną osobę
     */
    @GetMapping("/reviewed")
    public ResponseEntity<List<MovieReportItemRequest>> getUserReviewedMovies(@RequestParam Long userId) {
        return ResponseEntity.ok(movieService.getUserReviewedMovies(userId));
    }
}
