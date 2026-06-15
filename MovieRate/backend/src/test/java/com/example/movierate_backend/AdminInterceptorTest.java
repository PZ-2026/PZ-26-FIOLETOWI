package com.example.movierate_backend;

import com.example.movierate_backend.config.AdminInterceptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminInterceptorTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private AdminInterceptor adminInterceptor;

    @Test
    void preHandle_optionsRequest_returnsTrue() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("OPTIONS", "/api/admin/users");
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean result = adminInterceptor.preHandle(request, response, new Object());

        assertTrue(result);
        assertEquals(200, response.getStatus());
    }

    @Test
    void preHandle_missingUserIdHeader_returnsFalseAnd401() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/admin/users");
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean result = adminInterceptor.preHandle(request, response, new Object());

        assertFalse(result);
        assertEquals(401, response.getStatus());
    }

    @Test
    void preHandle_userIsAdmin_returnsTrue() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/admin/users");
        request.addHeader("X-User-Id", "1");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jdbcTemplate.queryForObject(anyString(), eq(String.class), eq(1L)))
                .thenReturn("ADMIN");

        boolean result = adminInterceptor.preHandle(request, response, new Object());

        assertTrue(result);
        assertEquals(200, response.getStatus());
    }

    @Test
    void preHandle_userIsRegularUser_returnsFalseAnd403() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/admin/users");
        request.addHeader("X-User-Id", "2");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jdbcTemplate.queryForObject(anyString(), eq(String.class), eq(2L)))
                .thenReturn("USER");

        boolean result = adminInterceptor.preHandle(request, response, new Object());

        assertFalse(result);
        assertEquals(403, response.getStatus());
    }

    @Test
    void preHandle_userNotFound_returnsFalseAnd403() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/admin/users");
        request.addHeader("X-User-Id", "999");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jdbcTemplate.queryForObject(anyString(), eq(String.class), eq(999L)))
                .thenThrow(new RuntimeException("Not found"));

        boolean result = adminInterceptor.preHandle(request, response, new Object());

        assertFalse(result);
        assertEquals(403, response.getStatus());
    }
}
