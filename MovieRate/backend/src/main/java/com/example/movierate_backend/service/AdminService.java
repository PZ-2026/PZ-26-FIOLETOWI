package com.example.movierate_backend.service;

import com.example.movierate_backend.dto.*;
import com.example.movierate_backend.model.User;
import com.example.movierate_backend.repository.UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final JdbcTemplate jdbcTemplate;
    private final UserRepository userRepository;

    public AdminService(JdbcTemplate jdbcTemplate, UserRepository userRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRepository = userRepository;
    }

    // --- Users ---

    public List<AdminUserResponse> getAllUsers() {
        String sql = """
                SELECT id, username, email, role, is_blocked,
                       created_at::TEXT AS created_at
                FROM users ORDER BY id ASC
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new AdminUserResponse(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("role"),
                        rs.getBoolean("is_blocked"),
                        rs.getString("created_at")
                ));
    }

    public void updateUserRole(Long userId, String role) {
        if (!role.equals("USER") && !role.equals("ADMIN")) {
            throw new IllegalArgumentException("Nieprawidłowa rola: " + role);
        }
        jdbcTemplate.update("UPDATE users SET role = ? WHERE id = ?", role, userId);
    }

    public void toggleUserBlock(Long userId) {
        jdbcTemplate.update("UPDATE users SET is_blocked = NOT is_blocked WHERE id = ?", userId);
    }

    public void deleteUser(Long userId) {
        jdbcTemplate.update("DELETE FROM users WHERE id = ?", userId);
    }

    // --- Movies ---

    public void createMovie(CreateMovieRequest request) {
        jdbcTemplate.update(
                "INSERT INTO movies (title, description, release_year, type) VALUES (?, ?, ?, ?)",
                request.getTitle(), request.getDescription(), request.getReleaseYear(), request.getType()
        );
    }

    public void updateMovie(Long movieId, CreateMovieRequest request) {
        jdbcTemplate.update(
                "UPDATE movies SET title = ?, description = ?, release_year = ?, type = ? WHERE id = ?",
                request.getTitle(), request.getDescription(), request.getReleaseYear(), request.getType(), movieId
        );
    }

    public void deleteMovie(Long movieId) {
        jdbcTemplate.update("DELETE FROM movies WHERE id = ?", movieId);
    }

    // --- Reviews ---

    public List<ReviewResponse> getAllReviews() {
        String sql = """
                SELECT rv.id, u.username, u.id AS user_id, m.title AS movie_title,
                       rv.content, rv.created_at::TEXT AS created_at, rv.is_deleted,
                       (SELECT r.rating FROM ratings r WHERE r.user_id = rv.user_id AND r.movie_id = rv.movie_id) AS user_rating,
                       u.profile_picture_url
                FROM reviews rv
                JOIN users u ON u.id = rv.user_id
                JOIN movies m ON m.id = rv.movie_id
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
                ));
    }

    public void approveReview(Long reviewId) {
        jdbcTemplate.update("UPDATE reviews SET is_deleted = FALSE WHERE id = ?", reviewId);
    }

    public void deleteReview(Long reviewId) {
        jdbcTemplate.update("UPDATE reviews SET is_deleted = TRUE WHERE id = ?", reviewId);
    }
}
