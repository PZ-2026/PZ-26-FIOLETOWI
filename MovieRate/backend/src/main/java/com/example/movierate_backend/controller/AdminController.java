package com.example.movierate_backend.controller;

import com.example.movierate_backend.dto.*;
import com.example.movierate_backend.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Kontroler REST odpowiedzialny za operacje administracyjne.
 * Umożliwia zarządzanie użytkownikami, filmami oraz recenzjami.
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    /**
     * Konstruktor wstrzykujący serwis administracyjny.
     * @param adminService serwis obsługujący logikę biznesową dla administratora
     */
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // --- Users ---

    /**
     * Pobiera listę wszystkich zarejestrowanych użytkowników.
     * @return lista obiektów AdminUserResponse reprezentujących użytkowników
     */
    @GetMapping("/users")
    public ResponseEntity<List<AdminUserResponse>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    /**
     * Aktualizuje rolę wskazanego użytkownika.
     * @param id identyfikator użytkownika
     * @param body mapa zawierająca nową rolę w kluczu "role"
     * @return odpowiedź z kodem 200 OK w przypadku sukcesu
     */
    @PutMapping("/users/{id}/role")
    public ResponseEntity<Void> updateUserRole(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        adminService.updateUserRole(id, body.get("role"));
        return ResponseEntity.ok().build();
    }

    /**
     * Zmienia status blokady wskazanego użytkownika (blokuje lub odblokowuje).
     * @param id identyfikator użytkownika
     * @return odpowiedź z kodem 200 OK w przypadku sukcesu
     */
    @PutMapping("/users/{id}/block")
    public ResponseEntity<Void> toggleUserBlock(@PathVariable Long id) {
        adminService.toggleUserBlock(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Trwale usuwa użytkownika z systemu.
     * @param id identyfikator użytkownika do usunięcia
     * @return odpowiedź z kodem 204 No Content w przypadku sukcesu
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // --- Genres ---

    @GetMapping("/genres")
    public ResponseEntity<List<Map<String, Object>>> getAllGenres() {
        return ResponseEntity.ok(adminService.getAllGenres());
    }

    // --- Movies ---

    /**
     * Tworzy nowy film w bazie danych.
     * @param request obiekt zawierający dane nowego filmu
     * @return odpowiedź z kodem 201 Created w przypadku sukcesu
     */
    @PostMapping("/movies")
    public ResponseEntity<Void> createMovie(@Valid @RequestBody CreateMovieRequest request) {
        adminService.createMovie(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Aktualizuje dane istniejącego filmu.
     * @param id identyfikator filmu do aktualizacji
     * @param request obiekt zawierający zaktualizowane dane filmu
     * @return odpowiedź z kodem 200 OK w przypadku sukcesu
     */
    @PutMapping("/movies/{id}")
    public ResponseEntity<Void> updateMovie(
            @PathVariable Long id,
            @Valid @RequestBody CreateMovieRequest request) {
        adminService.updateMovie(id, request);
        return ResponseEntity.ok().build();
    }

    /**
     * Usuwa film z systemu.
     * @param id identyfikator filmu do usunięcia
     * @return odpowiedź z kodem 204 No Content w przypadku sukcesu
     */
    @DeleteMapping("/movies/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        adminService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }

    // --- Reviews ---

    /**
     * Pobiera listę wszystkich recenzji dodanych przez użytkowników.
     * @return lista obiektów ReviewResponse
     */
    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewResponse>> getAllReviews() {
        return ResponseEntity.ok(adminService.getAllReviews());
    }

    /**
     * Zatwierdza wybraną recenzję (np. po uprzednim zablokowaniu lub jako proces weryfikacji).
     * @param id identyfikator recenzji
     * @return odpowiedź z kodem 200 OK w przypadku sukcesu
     */
    @PutMapping("/reviews/{id}/approve")
    public ResponseEntity<Void> approveReview(@PathVariable Long id) {
        adminService.approveReview(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Usuwa wybraną recenzję z systemu (moderacja).
     * @param id identyfikator recenzji do usunięcia
     * @return odpowiedź z kodem 204 No Content w przypadku sukcesu
     */
    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        adminService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
