package com.example.movierate_backend;

import com.example.movierate_backend.dto.MovieResponse;
import com.example.movierate_backend.service.MovieService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private MovieService movieService;

    @Test
    void getAllMovies_returnsMovieList() {
        MovieResponse movie1 = new MovieResponse(1L, "Test Movie", "Description", 2024, "Film", 8.5, null, null);
        MovieResponse movie2 = new MovieResponse(2L, "Another Movie", "Desc", 2023, "Serial", 7.2, null, null);

        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(List.of(movie1, movie2));

        List<MovieResponse> result = movieService.getAllMovies();

        assertEquals(2, result.size());
        assertEquals("Test Movie", result.get(0).getTitle());
        assertEquals("Another Movie", result.get(1).getTitle());
    }

    @Test
    void getTopRatedMovies_returnsLimitedMovies() {
        MovieResponse movie = new MovieResponse(1L, "Top Movie", "Great", 2024, "Film", 9.5, null, null);

        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyInt())).thenReturn(List.of(movie));

        List<MovieResponse> result = movieService.getTopRatedMovies(1);

        assertEquals(1, result.size());
        assertEquals("Top Movie", result.get(0).getTitle());
        assertEquals(9.5, result.get(0).getAverageRating());
    }

    @Test
    void searchMovies_withQueryOnly_returnsFilteredMovies() {
        MovieResponse movie = new MovieResponse(1L, "Found Movie", "Desc", 2024, "Film", 8.0, null, null);

        when(jdbcTemplate.query(anyString(), any(RowMapper.class), any(Object[].class)))
                .thenReturn(List.of(movie));

        List<MovieResponse> result = movieService.searchMovies("found", null, null);

        assertEquals(1, result.size());
        assertEquals("Found Movie", result.get(0).getTitle());
    }

    @Test
    void searchMovies_withAllFilters_returnsFilteredMovies() {
        MovieResponse movie = new MovieResponse(1L, "Inception", "Dreams", 2010, "Film", 9.0, null, null);

        when(jdbcTemplate.query(anyString(), any(RowMapper.class), any(Object[].class)))
                .thenReturn(List.of(movie));

        List<MovieResponse> result = movieService.searchMovies("inception", "Film", 2010);

        assertEquals(1, result.size());
        assertEquals("Inception", result.get(0).getTitle());
    }

    @Test
    void searchMovies_withNoMatch_returnsEmptyList() {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), any(Object[].class)))
                .thenReturn(List.of());

        List<MovieResponse> result = movieService.searchMovies("nonexistent", null, null);

        assertTrue(result.isEmpty());
    }
}
