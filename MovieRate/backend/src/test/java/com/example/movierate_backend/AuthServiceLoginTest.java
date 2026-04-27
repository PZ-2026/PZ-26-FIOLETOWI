package com.example.movierate_backend;

import com.example.movierate_backend.dto.AuthResponse;
import com.example.movierate_backend.dto.LoginRequest;
import com.example.movierate_backend.model.User;
import com.example.movierate_backend.repository.UserRepository;
import com.example.movierate_backend.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceLoginTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    @Test
    void login_returnsUserData_whenCredentialsAreCorrect() {
        User user = new User();
        user.setUsername("admin");
        user.setEmail("admin@example.com");
        user.setPasswordHash(BCrypt.hashpw("1234", BCrypt.gensalt()));
        user.setRole("ADMIN");

        LoginRequest request = new LoginRequest();
        request.setEmail("admin@example.com");
        request.setPassword("1234");

        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(user));

        AuthResponse response = authService.login(request);

        assertEquals("Login successful", response.getMessage());
        assertEquals("admin", response.getUsername());
        assertEquals("admin@example.com", response.getEmail());
        assertEquals("ADMIN", response.getRole());
    }

    @Test
    void login_throwsException_whenPasswordIsIncorrect() {
        User user = new User();
        user.setUsername("admin");
        user.setEmail("admin@example.com");
        user.setPasswordHash(BCrypt.hashpw("1234", BCrypt.gensalt()));
        user.setRole("ADMIN");

        LoginRequest request = new LoginRequest();
        request.setEmail("admin@example.com");
        request.setPassword("wrong-password");

        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> authService.login(request));
    }
}
