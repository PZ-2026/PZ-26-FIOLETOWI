package com.example.movierate_backend.controller;

import com.example.movierate_backend.dto.*;
import com.example.movierate_backend.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // --- Users ---

    @GetMapping("/users")
    public ResponseEntity<List<AdminUserResponse>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<Void> updateUserRole(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        adminService.updateUserRole(id, body.get("role"));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/users/{id}/block")
    public ResponseEntity<Void> toggleUserBlock(@PathVariable Long id) {
        adminService.toggleUserBlock(id);
        return ResponseEntity.ok().build();
    }

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

    @PostMapping("/movies")
    public ResponseEntity<Void> createMovie(@Valid @RequestBody CreateMovieRequest request) {
        adminService.createMovie(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/movies/{id}")
    public ResponseEntity<Void> updateMovie(
            @PathVariable Long id,
            @Valid @RequestBody CreateMovieRequest request) {
        adminService.updateMovie(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/movies/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        adminService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }

    // --- Reviews ---

    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewResponse>> getAllReviews() {
        return ResponseEntity.ok(adminService.getAllReviews());
    }

    @PutMapping("/reviews/{id}/approve")
    public ResponseEntity<Void> approveReview(@PathVariable Long id) {
        adminService.approveReview(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        adminService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
