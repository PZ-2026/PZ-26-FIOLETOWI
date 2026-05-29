package com.example.movierate_backend;

import com.example.movierate_backend.dto.*;
import com.example.movierate_backend.model.User;
import com.example.movierate_backend.repository.UserRepository;
import com.example.movierate_backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void getUserStats_returnsStats() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Long.class), anyLong()))
                .thenReturn(5L, 12L, 3L, 4L);

        UserStatsResponse stats = userService.getUserStats(1L);

        assertEquals(5, stats.getWatchedCount());
        assertEquals(12, stats.getRatingCount());
        assertEquals(3, stats.getReviewCount());
        assertEquals(4, stats.getListCount());
    }

    @Test
    void getUserStats_returnsZeroOnNull() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Long.class), anyLong()))
                .thenReturn(null, null, null, null);

        UserStatsResponse stats = userService.getUserStats(1L);

        assertEquals(0, stats.getWatchedCount());
        assertEquals(0, stats.getRatingCount());
        assertEquals(0, stats.getReviewCount());
        assertEquals(0, stats.getListCount());
    }

    @Test
    void getUserGenres_returnsGenreStats() {
        GenreStatResponse genre1 = new GenreStatResponse("Dramat", 10L);
        GenreStatResponse genre2 = new GenreStatResponse("Akcja", 5L);

        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyLong()))
                .thenReturn(List.of(genre1, genre2));

        List<GenreStatResponse> result = userService.getUserGenres(1L);

        assertEquals(2, result.size());
        assertEquals("Dramat", result.get(0).getName());
        assertEquals(10, result.get(0).getCount());
    }

    @Test
    void getUserActivity_returnsActivityList() {
        ActivityResponse activity1 = new ActivityResponse("rating", "Movie 1", "8", "2024-01-01");
        ActivityResponse activity2 = new ActivityResponse("review", "Movie 2", "Great film", "2024-01-02");

        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyLong(), anyLong(), anyLong()))
                .thenReturn(List.of(activity1, activity2));

        List<ActivityResponse> result = userService.getUserActivity(1L);

        assertEquals(2, result.size());
        assertEquals("rating", result.get(0).getType());
        assertEquals("review", result.get(1).getType());
    }

    @Test
    void updateProfile_updatesUser() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("oldname");
        existingUser.setEmail("old@example.com");

        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setUsername("newname");
        request.setEmail("new@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.updateProfile(1L, request);

        assertEquals("newname", result.getUsername());
        assertEquals("new@example.com", result.getEmail());
    }

    @Test
    void updateProfile_throwsWhenUserNotFound() {
        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setUsername("test");
        request.setEmail("test@example.com");

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.updateProfile(99L, request));
    }
}
