package com.example.movierate_backend;

import com.example.movierate_backend.model.User;
import com.example.movierate_backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.sql.init.mode=never",
        "app.seed.enabled=false"
})
class DatabaseConnectionTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserRepository userRepository;

    @Test
    void backend_connectsToDatabase_andPersistsUser() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            assertTrue(connection.isValid(2));
        }

        User user = new User();
        user.setUsername("anna");
        user.setEmail("anna@example.com");
        user.setPasswordHash("hashed-password");
        user.setRole("USER");

        userRepository.save(user);

        User foundUser = userRepository.findByEmail("anna@example.com").orElse(null);

        assertNotNull(foundUser);
        assertEquals("anna", foundUser.getUsername());
    }
}
