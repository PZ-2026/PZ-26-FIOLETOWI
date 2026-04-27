package com.example.movierate_backend;

import com.example.movierate_backend.dto.AuthResponse;
import com.example.movierate_backend.dto.RegisterRequest;
import com.example.movierate_backend.model.User;
import com.example.movierate_backend.repository.UserRepository;
import com.example.movierate_backend.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceRegisterTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_hashesPassword_andAssignsDefaultRole() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("anna");
        request.setEmail("anna@example.com");
        request.setPassword("secret123");

        when(userRepository.findByEmail("anna@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AuthResponse response = authService.register(request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals("Registration successful", response.getMessage());
        assertEquals("anna", response.getUsername());
        assertEquals("anna@example.com", response.getEmail());
        assertEquals("USER", response.getRole());
        assertNotEquals("secret123", savedUser.getPasswordHash());
        assertTrue(BCrypt.checkpw("secret123", savedUser.getPasswordHash()));
        assertEquals("USER", savedUser.getRole());
    }
}
