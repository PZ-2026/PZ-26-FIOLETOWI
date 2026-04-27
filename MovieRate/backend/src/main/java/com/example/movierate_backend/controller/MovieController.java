package com.example.movierate_backend.controller;

import com.example.movierate_backend.dto.MovieResponse;
import com.example.movierate_backend.service.MovieService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public List<MovieResponse> getAllMovies() {
        return movieService.getAllMovies();
    }

    @GetMapping("/top-rated")
    public List<MovieResponse> getTopRatedMovies(@RequestParam(defaultValue = "10") int limit) {
        return movieService.getTopRatedMovies(limit);
    }
}
