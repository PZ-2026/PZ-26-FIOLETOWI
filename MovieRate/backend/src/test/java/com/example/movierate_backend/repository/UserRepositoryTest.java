package com.example.movierate_backend.repository;

import com.example.movierate_backend.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest(properties = {
        "spring.sql.init.mode=never",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByEmail_returnsPersistedUserFromDatabase() {
        User user = new User();
        user.setUsername("anna");
        user.setEmail("anna@example.com");
        user.setPasswordHash("hashed-password");
        user.setRole("USER");

        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByEmail("anna@example.com");

        assertTrue(foundUser.isPresent());
        assertEquals("anna", foundUser.get().getUsername());
        assertEquals("USER", foundUser.get().getRole());
    }
}
