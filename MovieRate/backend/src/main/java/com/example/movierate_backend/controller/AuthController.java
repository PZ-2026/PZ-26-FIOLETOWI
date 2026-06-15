package com.example.movierate_backend.controller;

import com.example.movierate_backend.dto.AuthResponse;
import com.example.movierate_backend.dto.LoginRequest;
import com.example.movierate_backend.dto.RegisterRequest;
import com.example.movierate_backend.dto.UpdateProfileRequest;
import com.example.movierate_backend.model.User;
import com.example.movierate_backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

/**
 * Kontroler REST odpowiedzialny za uwierzytelnianie i autoryzację użytkowników.
 * Obsługuje operacje logowania, rejestracji oraz aktualizacji profilu.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * Konstruktor wstrzykujący serwis uwierzytelniający.
     * @param authService serwis obsługujący procesy rejestracji i logowania
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Uwierzytelnia użytkownika na podstawie adresu email i hasła.
     * @param request obiekt zawierający dane logowania (email, hasło)
     * @return odpowiedź zawierająca dane uwierzytelnionego użytkownika (AuthResponse) lub komunikat błędu
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Rejestruje nowego użytkownika w systemie.
     * @param request obiekt zawierający dane do rejestracji (nazwa użytkownika, email, hasło)
     * @return odpowiedź zawierająca nowo utworzone dane użytkownika (AuthResponse) lub komunikat błędu
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/status")
    public ResponseEntity<?> status(@RequestParam Long userId) {
        try {
            return ResponseEntity.ok(authService.getActiveUserStatus(userId));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Aktualizuje profil istniejącego użytkownika.
     * @param userId identyfikator aktualizowanego użytkownika przekazywany jako parametr zapytania
     * @param request obiekt zawierający nowe dane profilowe
     * @return zaktualizowane dane użytkownika (AuthResponse) lub komunikat błędu w przypadku braku walidacji
     */
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestParam Long userId, @Valid @RequestBody UpdateProfileRequest request) {
        try {
            User updated = authService.updateProfile(userId, request);
            AuthResponse response = authService.toAuthResponse(updated, "Profil zaktualizowany");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Globalny handler błędów walidacji argumentów dla tego kontrolera.
     * Zwraca czytelne komunikaty wygenerowane przez adnotacje @Valid.
     * @param exception wyjątek z błędami walidacji
     * @return odpowiedź z kodem 400 Bad Request oraz wiadomością ze wszystkimi błędami połączonymi w jeden ciąg tekstowy
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationErrors(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .distinct()
                .collect(Collectors.joining(". "));

        return ResponseEntity.badRequest().body(message);
    }
}
