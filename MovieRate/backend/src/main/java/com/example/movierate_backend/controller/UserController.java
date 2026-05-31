package com.example.movierate_backend.controller;

import com.example.movierate_backend.dto.*;
import com.example.movierate_backend.model.User;
import com.example.movierate_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Kontroler REST dostarczający informacje szczegółowe i metryki o pojedynczych użytkownikach.
 * Generuje statystyki używane na m.in. stronach profilowych użytkownika.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    /**
     * Konstruktor z wstrzykiwaniem zależności UserService.
     * @param userService serwis gromadzący dane o użytkownikach i generujący statystyki
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Zwraca zbiór podstawowych liczbowych statystyk aktywności danego użytkownika (liczba filmów, średnia ocena itp.).
     * @param id identyfikator użytkownika
     * @return pakiet powiązanych statystyk profilowych
     */
    @GetMapping("/{id}/stats")
    public ResponseEntity<UserStatsResponse> getUserStats(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserStats(id));
    }

    /**
     * Oblicza preferowane przez usera gatunki filmowe i częstotliwość ich oceniania.
     * @param id identyfikator użytkownika do przeanalizowania gustu filmowego
     * @return ułożona statystyka gatunków
     */
    @GetMapping("/{id}/genres")
    public ResponseEntity<List<GenreStatResponse>> getUserGenres(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserGenres(id));
    }

    /**
     * Generuje listę osi czasu ostatnich akcji (aktywności) powiązanych z kontem.
     * Może obejmować m.in. ostatnio ocenione produkcje.
     * @param id identyfikator analizowanego użytkownika
     * @return zebrana lista zarejestrowanej aktywności ułożona z reguły chronologicznie
     */
    @GetMapping("/{id}/activity")
    public ResponseEntity<List<ActivityResponse>> getUserActivity(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserActivity(id));
    }
}
