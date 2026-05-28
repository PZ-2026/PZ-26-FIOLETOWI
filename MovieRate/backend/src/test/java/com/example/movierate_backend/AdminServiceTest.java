package com.example.movierate_backend;

import com.example.movierate_backend.dto.*;
import com.example.movierate_backend.repository.UserRepository;
import com.example.movierate_backend.service.AdminService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminService adminService;

    @Test
    void getAllUsers_returnsUserList() {
        AdminUserResponse user1 = new AdminUserResponse(1L, "user1", "user1@example.com", "USER", false, "2024-01-01");
        AdminUserResponse user2 = new AdminUserResponse(2L, "admin1", "admin@example.com", "ADMIN", false, "2024-01-02");

        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(List.of(user1, user2));

        List<AdminUserResponse> result = adminService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getUsername());
        assertEquals("ADMIN", result.get(1).getRole());
    }

    @Test
    void updateUserRole_updatesToAdmin() {
        adminService.updateUserRole(1L, "ADMIN");

        verify(jdbcTemplate).update("UPDATE users SET role = ? WHERE id = ?", "ADMIN", 1L);
    }

    @Test
    void updateUserRole_updatesToUser() {
        adminService.updateUserRole(1L, "USER");

        verify(jdbcTemplate).update("UPDATE users SET role = ? WHERE id = ?", "USER", 1L);
    }

    @Test
    void updateUserRole_throwsOnInvalidRole() {
        assertThrows(IllegalArgumentException.class, () -> adminService.updateUserRole(1L, "INVALID"));
    }

    @Test
    void toggleUserBlock_togglesIsBlocked() {
        adminService.toggleUserBlock(1L);

        verify(jdbcTemplate).update("UPDATE users SET is_blocked = NOT is_blocked WHERE id = ?", 1L);
    }

    @Test
    void deleteUser_deletesUser() {
        adminService.deleteUser(1L);

        verify(jdbcTemplate).update("DELETE FROM users WHERE id = ?", 1L);
    }

    @Test
    void createMovie_insertsMovie() {
        CreateMovieRequest request = new CreateMovieRequest();
        request.setTitle("New Movie");
        request.setDescription("A great movie");
        request.setReleaseYear(2024);
        request.setType("Film");

        adminService.createMovie(request);

        verify(jdbcTemplate).update(
                "INSERT INTO movies (title, description, release_year, type) VALUES (?, ?, ?, ?)",
                "New Movie", "A great movie", 2024, "Film"
        );
    }

    @Test
    void updateMovie_updatesMovie() {
        CreateMovieRequest request = new CreateMovieRequest();
        request.setTitle("Updated Title");
        request.setDescription("Updated desc");
        request.setReleaseYear(2023);
        request.setType("Serial");

        adminService.updateMovie(1L, request);

        verify(jdbcTemplate).update(
                "UPDATE movies SET title = ?, description = ?, release_year = ?, type = ? WHERE id = ?",
                "Updated Title", "Updated desc", 2023, "Serial", 1L
        );
    }

    @Test
    void deleteMovie_deletesMovie() {
        adminService.deleteMovie(1L);

        verify(jdbcTemplate).update("DELETE FROM movies WHERE id = ?", 1L);
    }

    @Test
    void getAllReviews_returnsReviewList() {
        ReviewResponse review1 = new ReviewResponse(1L, "user1", "Movie 1", "Great!", "2024-01-01", false);
        ReviewResponse review2 = new ReviewResponse(2L, "user2", "Movie 2", "Nice", "2024-01-02", false);

        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(List.of(review1, review2));

        List<ReviewResponse> result = adminService.getAllReviews();

        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getUsername());
        assertEquals("Movie 2", result.get(1).getMovieTitle());
    }

    @Test
    void approveReview_setsNotDeleted() {
        adminService.approveReview(1L);

        verify(jdbcTemplate).update("UPDATE reviews SET is_deleted = FALSE WHERE id = ?", 1L);
    }

    @Test
    void deleteReview_setsDeleted() {
        adminService.deleteReview(1L);

        verify(jdbcTemplate).update("UPDATE reviews SET is_deleted = TRUE WHERE id = ?", 1L);
    }
}
