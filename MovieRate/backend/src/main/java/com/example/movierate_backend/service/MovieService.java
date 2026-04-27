package com.example.movierate_backend.service;

import com.example.movierate_backend.dto.MovieResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class MovieService {

    private final JdbcTemplate jdbcTemplate;

    public MovieService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<MovieResponse> getAllMovies() {
        return jdbcTemplate.query(baseMovieQuery() + " ORDER BY m.title ASC", this::mapMovie);
    }

    public List<MovieResponse> getTopRatedMovies(int limit) {
        return jdbcTemplate.query(
                baseMovieQuery() + " ORDER BY average_rating DESC NULLS LAST, m.title ASC LIMIT ?",
                this::mapMovie,
                limit
        );
    }

    private String baseMovieQuery() {
        return """
                SELECT
                    m.id,
                    m.title,
                    m.description,
                    m.release_year,
                    m.type,
                    COALESCE(ROUND(AVG(r.rating)::numeric, 1), 0) AS average_rating
                FROM movies m
                LEFT JOIN ratings r ON r.movie_id = m.id
                GROUP BY m.id, m.title, m.description, m.release_year, m.type
                """;
    }

    private MovieResponse mapMovie(ResultSet rs, int rowNum) throws SQLException {
        return new MovieResponse(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getInt("release_year"),
                rs.getString("type"),
                rs.getDouble("average_rating")
        );
    }
}
