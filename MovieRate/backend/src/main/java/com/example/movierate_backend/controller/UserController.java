package com.example.movierate_backend.controller;

import com.example.movierate_backend.dto.*;
import com.example.movierate_backend.model.User;
import com.example.movierate_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<UserStatsResponse> getUserStats(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserStats(id));
    }

    @GetMapping("/{id}/genres")
    public ResponseEntity<List<GenreStatResponse>> getUserGenres(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserGenres(id));
    }

    @GetMapping("/{id}/activity")
    public ResponseEntity<List<ActivityResponse>> getUserActivity(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserActivity(id));
    }
}
