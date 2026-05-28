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

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public List<MovieResponse> getAllMovies() {
        return movieService.getAllMovies();
    }

    @GetMapping("/top-rated")
    public List<MovieResponse> getTopRatedMovies(@RequestParam(defaultValue = "10") int limit) {
        return movieService.getTopRatedMovies(limit);
    }

    @GetMapping("/newest")
    public List<MovieResponse> getNewestMovies(@RequestParam(defaultValue = "10") int limit) {
        return movieService.getNewestMovies(limit);
    }

    @GetMapping("/by-genre")
    public List<MovieResponse> getMoviesByGenre(
            @RequestParam String genre,
            @RequestParam(defaultValue = "10") int limit) {
        return movieService.getMoviesByGenre(genre, limit);
    }

    @GetMapping("/{id}/cast")
    public List<CastMemberResponse> getMovieCast(@PathVariable Long id) {
        return movieService.getMovieCast(id);
    }

    @GetMapping("/search")
    public List<MovieResponse> searchMovies(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer year) {
        return movieService.searchMovies(q, type, year);
    }

    @GetMapping("/{id}/rating")
    public ResponseEntity<RatingResponse> getUserRating(
            @PathVariable Long id,
            @RequestParam Long userId) {
        return ResponseEntity.ok(movieService.getUserRating(id, userId));
    }

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

    @DeleteMapping("/{id}/rating")
    public ResponseEntity<RatingResponse> deleteRating(
            @PathVariable Long id,
            @RequestParam Long userId) {
        return ResponseEntity.ok(movieService.deleteRating(id, userId));
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<List<ReviewResponse>> getMovieReviews(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.getMovieReviews(id));
    }

    @PostMapping("/{id}/reviews")
    public ResponseEntity<Void> addReview(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body) {
        Long userId = Long.valueOf(body.get("userId").toString());
        String content = body.get("content").toString();
        movieService.addReview(id, userId, content);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long id,
            @PathVariable Long reviewId,
            @RequestParam Long userId) {
        movieService.deleteReview(reviewId, userId);
        return ResponseEntity.noContent().build();
    }

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

    @GetMapping("/rated")
    public ResponseEntity<List<MovieReportItemRequest>> getUserRatedMovies(@RequestParam Long userId) {
        return ResponseEntity.ok(movieService.getUserRatedMovies(userId));
    }

    @GetMapping("/reviewed")
    public ResponseEntity<List<MovieReportItemRequest>> getUserReviewedMovies(@RequestParam Long userId) {
        return ResponseEntity.ok(movieService.getUserReviewedMovies(userId));
    }
}
