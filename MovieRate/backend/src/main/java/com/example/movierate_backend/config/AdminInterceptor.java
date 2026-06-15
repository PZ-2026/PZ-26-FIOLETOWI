package com.example.movierate_backend.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    private final JdbcTemplate jdbcTemplate;

    public AdminInterceptor(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Zezwól na zapytania typu OPTIONS (CORS preflight)
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String userIdHeader = request.getHeader("X-User-Id");
        if (userIdHeader == null || userIdHeader.isBlank()) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Brak identyfikatora użytkownika w nagłówku X-User-Id.");
            return false;
        }

        try {
            Long userId = Long.parseLong(userIdHeader.trim());
            String role = jdbcTemplate.queryForObject(
                    "SELECT role FROM users WHERE id = ?",
                    String.class,
                    userId
            );

            if ("ADMIN".equalsIgnoreCase(role)) {
                return true;
            }
        } catch (Exception e) {
            // Użytkownik nie istnieje lub błąd parsowania ID
        }

        response.sendError(HttpStatus.FORBIDDEN.value(), "Brak uprawnień administratora.");
        return false;
    }
}
