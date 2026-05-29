package com.example.movierate_backend.service;

import com.example.movierate_backend.dto.*;
import com.example.movierate_backend.model.User;
import com.example.movierate_backend.repository.UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final JdbcTemplate jdbcTemplate;
    private final UserRepository userRepository;

    public UserService(JdbcTemplate jdbcTemplate, UserRepository userRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRepository = userRepository;
    }

    public UserStatsResponse getUserStats(Long userId) {
        Long watchedCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM user_list_items uli " +
                "JOIN user_lists ul ON ul.id = uli.list_id " +
                "WHERE ul.user_id = ? AND ul.type = 'WATCHED'",
                Long.class, userId
        );

        Long ratingCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM ratings WHERE user_id = ?",
                Long.class, userId
        );

        Long reviewCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM reviews WHERE user_id = ? AND is_deleted = FALSE",
                Long.class, userId
        );

        Long listCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM user_lists WHERE user_id = ?",
                Long.class, userId
        );

        return new UserStatsResponse(
                watchedCount != null ? watchedCount : 0,
                ratingCount != null ? ratingCount : 0,
                reviewCount != null ? reviewCount : 0,
                listCount != null ? listCount : 0
        );
    }

    public List<GenreStatResponse> getUserGenres(Long userId) {
        String sql = """
                SELECT g.name, COUNT(*) AS count
                FROM genres g
                JOIN movie_genres mg ON mg.genre_id = g.id
                JOIN ratings r ON r.movie_id = mg.movie_id
                WHERE r.user_id = ?
                GROUP BY g.name
                ORDER BY count DESC
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new GenreStatResponse(rs.getString("name"), rs.getLong("count")), userId);
    }

    public List<ActivityResponse> getUserActivity(Long userId) {
        String sql = """
                (SELECT 'rating' AS type, m.title AS movie_title,
                        CAST(r.rating AS VARCHAR) AS detail, r.created_at::TEXT AS date
                 FROM ratings r JOIN movies m ON m.id = r.movie_id WHERE r.user_id = ?)
                UNION ALL
                (SELECT 'review' AS type, m.title AS movie_title,
                        LEFT(rv.content, 50) AS detail, rv.created_at::TEXT AS date
                 FROM reviews rv JOIN movies m ON m.id = rv.movie_id
                 WHERE rv.user_id = ? AND rv.is_deleted = FALSE)
                UNION ALL
                (SELECT 'list_add' AS type, m.title AS movie_title,
                        ul.name AS detail, m.created_at::TEXT AS date
                 FROM user_list_items uli
                 JOIN movies m ON m.id = uli.movie_id
                 JOIN user_lists ul ON ul.id = uli.list_id
                 WHERE ul.user_id = ?)
                ORDER BY date DESC LIMIT 10
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new ActivityResponse(
                        rs.getString("type"),
                        rs.getString("movie_title"),
                        rs.getString("detail"),
                        rs.getString("date")
                ), userId, userId, userId);
    }

    public User updateProfile(Long userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Użytkownik nie znaleziony"));

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        return userRepository.save(user);
    }
}
