package com.example.movierate_backend.service;

import com.example.movierate_backend.dto.AuthResponse;
import com.example.movierate_backend.dto.LoginRequest;
import com.example.movierate_backend.dto.RegisterRequest;
import com.example.movierate_backend.dto.UpdateProfileRequest;
import com.example.movierate_backend.model.User;
import com.example.movierate_backend.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Serwis autoryzacyjny odpowiadający za logikę biznesową uwierzytelniania, rejestracji oraz manipulacji profilem.
 * Wykorzystuje kryptografię (BCrypt) do sprawdzania i zapisywania haszy haseł.
 */
@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;

    /**
     * Konstruktor wstrzykujący zależność UserRepository.
     * @param userRepository mechanizm łączenia z tabelą użytkowników (Spring Data JPA)
     */
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Logika logowania użytkownika. Rozwiązuje problem wielkości znaków i porównuje hasze haseł.
     * Dodatkowo tworzy zapisy z diagnostyką logowań do panelu dewelopera (Logger).
     * @param request żądanie ze wstrzykniętymi danymi do uwierzytelnienia (email, surowe hasło)
     * @return udane żądanie zostaje nagrodzone poprawnym obiektem AuthResponse z metadanymi profilu
     * @throws IllegalArgumentException w przypadku nieprawidłowego adresu e-mail lub gdy test hasła się nie powiedzie
     */
    public AuthResponse login(LoginRequest request) {
        logger.trace("Received login request");

        String email = request.getEmail().trim().toLowerCase();
        String maskedEmail = maskEmail(email);
        logger.debug("Normalized login email={}", maskedEmail);

        Optional<User> optionalUser;
        try {
            optionalUser = userRepository.findByEmail(email);
        } catch (RuntimeException exception) {
            logger.error("Login failed while looking up user email={}", maskedEmail, exception);
            throw exception;
        }

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            logger.trace("Found user account for login email={}", maskedEmail);

            boolean match = BCrypt.checkpw(request.getPassword(), user.getPasswordHash());

            if (match) {
                if (Boolean.TRUE.equals(user.getBlocked())) {
                    logger.warn("Login blocked because account is disabled email={}", maskedEmail);
                    throw new IllegalStateException("Konto jest zablokowane");
                }

                logger.info("User login successful email={} role={}", maskedEmail, user.getRole());
                return toAuthResponse(user, "Login successful");
            }

            logger.warn("Login failed because password did not match email={}", maskedEmail);
        } else {
            logger.warn("Login failed because user was not found email={}", maskedEmail);
        }

        throw new IllegalArgumentException("Nieprawidlowy adres e-mail lub haslo");
    }

    /**
     * Konfiguruje i rejestruje nowego użytkownika, dokonując przy tym podstawowej weryfikacji zajętości e-maila
     * oraz hashowania hasła przy wykorzystaniu biblioteki BCrypt.
     * @param request dane nadesłane w formularzu rejestracyjnym
     * @return zatwierdzony profil zapisany do bazy danych z przypisaną domyślną rolą
     * @throws IllegalArgumentException przy kolizji poczty email lub pustych danych
     */
    public AuthResponse register(RegisterRequest request) {
        logger.trace("Received registration request");

        String email = request.getEmail().trim().toLowerCase();
        String username = request.getUsername().trim();
        String maskedEmail = maskEmail(email);
        logger.debug("Normalized registration data username={} email={}", username, maskedEmail);

        Optional<User> existingUser;
        try {
            existingUser = userRepository.findByEmail(email);
        } catch (RuntimeException exception) {
            logger.error("Registration failed while checking existing user email={}", maskedEmail, exception);
            throw exception;
        }

        if (existingUser.isPresent()) {
            logger.warn("Registration failed because email is already taken email={}", maskedEmail);
            throw new IllegalArgumentException("Adres e-mail jest juz zajety");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        logger.trace("Hashing password for new user email={}", maskedEmail);
        String hashedPassword = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());
        user.setPasswordHash(hashedPassword);
        user.setRole("USER"); // Default role

        User savedUser;
        try {
            savedUser = userRepository.save(user);
            logger.info("User registration successful email={} role={}", maskedEmail, savedUser.getRole());
        } catch (RuntimeException exception) {
            logger.error("Registration failed while saving user email={}", maskedEmail, exception);
            throw exception;
        }

        return toAuthResponse(savedUser, "Registration successful");
    }

    public AuthResponse getActiveUserStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Użytkownik nie znaleziony"));

        if (Boolean.TRUE.equals(user.getBlocked())) {
            throw new IllegalStateException("Konto jest zablokowane");
        }

        return toAuthResponse(user, "Session active");
    }

    public AuthResponse toAuthResponse(User user, String message) {
        return new AuthResponse(
                user.getId(),
                message,
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt() != null ? user.getCreatedAt().toString() : null,
                user.getProfilePictureUrl(),
                Boolean.TRUE.equals(user.getBlocked())
        );
    }

    /**
     * Weryfikuje oraz dokonuje nadpisania danych w panelu profilu. Gwarantuje także mechanikę weryfikacji maila
     * by nie nastąpiła kolizja z adresem przypisanym już do innej osoby w serwisie.
     * @param userId konto poddawane modyfikacjom
     * @param request zestawienie nowych i istniejących atrybutów profilu
     * @return referencja nadpisanego obiektu użytkownika w bazie JPA
     * @throws IllegalArgumentException jeśli użytkownik nie istnieje w bazie lub nowy mail narusza unikalność
     */
    public User updateProfile(Long userId, UpdateProfileRequest request) {
        logger.trace("Updating profile for userId={}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.warn("Profile update failed: user not found id={}", userId);
                    return new IllegalArgumentException("Użytkownik nie znaleziony");
                });

        // Check if email is already taken by another user
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail().trim().toLowerCase());
        if (existingUser.isPresent() && !existingUser.get().getId().equals(userId)) {
            logger.warn("Profile update failed: email already taken email={}", maskEmail(request.getEmail()));
            throw new IllegalArgumentException("Adres e-mail jest już zajęty");
        }

        user.setUsername(request.getUsername().trim());
        user.setEmail(request.getEmail().trim().toLowerCase());

        // Update password if provided
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            String hashedPassword = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());
            user.setPasswordHash(hashedPassword);
            logger.trace("Password updated for userId={}", userId);
        }

        // Update profile picture URL if provided
        if (request.getProfilePictureUrl() != null) {
            user.setProfilePictureUrl(request.getProfilePictureUrl());
            logger.trace("Profile picture updated for userId={}", userId);
        }

        User savedUser = userRepository.save(user);
        logger.info("Profile updated successfully userId={}", userId);
        return savedUser;
    }

    /**
     * Narzędzie pomocnicze odpowiedzialne za maskowanie (ukrywanie) wrażliwego adresu e-mail
     * wykorzystywanego w wyjściu logów dla dodatkowego zapewnienia bezpieczeństwa z prawem do prywatności.
     * @param email wprowadzony czysty strumień znaków reprezentujący email
     * @return zamazany tekst do wykorzystania w konsoli np. t***@gmail.com
     */
    private String maskEmail(String email) {
        int atIndex = email.indexOf('@');
        if (atIndex <= 1) {
            return "***" + email.substring(Math.max(atIndex, 0));
        }

        String localPart = email.substring(0, atIndex);
        String domain = email.substring(atIndex);
        return localPart.charAt(0) + "***" + domain;
    }
}
