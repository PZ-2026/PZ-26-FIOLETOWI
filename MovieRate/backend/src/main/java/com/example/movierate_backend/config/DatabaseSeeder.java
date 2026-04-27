package com.example.movierate_backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private record SeedMovieRef(String title, int releaseYear, String type) {}

    private final JdbcTemplate jdbcTemplate;
    private final boolean seedEnabled;

    public DatabaseSeeder(JdbcTemplate jdbcTemplate, @Value("${app.seed.enabled:true}") boolean seedEnabled) {
        this.jdbcTemplate = jdbcTemplate;
        this.seedEnabled = seedEnabled;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (!seedEnabled) {
            return;
        }

        seedUsers();
        seedGenres();
        seedMovies();
        seedMovieGenres();
        seedRoles();
        seedPeople();
        seedMovieCast();
        seedRatings();
        seedReviews();
        seedUserLists();
        seedUserListItems();
        seedReports();
    }

    private void seedUsers() {
        String passwordHash = "$2a$10$WVHMrwX2wpE0SveYBbOlbu5dM.HyYFR4OWCRELYtyk8AFEixIUiUe";

        upsertUser("user1", "user1@example.com", passwordHash, "USER");
        upsertUser("user2", "user2@example.com", passwordHash, "USER");
        upsertUser("user3", "user3@example.com", passwordHash, "USER");
        upsertUser("user4", "user4@example.com", passwordHash, "USER");
        upsertUser("user5", "user5@example.com", passwordHash, "USER");
        upsertUser("user6", "user6@example.com", passwordHash, "USER");
        upsertUser("user7", "user7@example.com", passwordHash, "USER");
        upsertUser("user8", "user8@example.com", passwordHash, "USER");
        upsertUser("user9", "user9@example.com", passwordHash, "USER");
        upsertUser("admin", "admin@example.com", passwordHash, "ADMIN");
    }

    private void seedGenres() {
        insertIfMissing("INSERT INTO genres (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = ?)",
                "Action", "Action");
        insertIfMissing("INSERT INTO genres (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = ?)",
                "Drama", "Drama");
        insertIfMissing("INSERT INTO genres (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = ?)",
                "Comedy", "Comedy");
        insertIfMissing("INSERT INTO genres (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = ?)",
                "Sci-Fi", "Sci-Fi");
        insertIfMissing("INSERT INTO genres (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = ?)",
                "Horror", "Horror");
        insertIfMissing("INSERT INTO genres (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = ?)",
                "Thriller", "Thriller");
        insertIfMissing("INSERT INTO genres (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = ?)",
                "Romance", "Romance");
        insertIfMissing("INSERT INTO genres (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = ?)",
                "Fantasy", "Fantasy");
        insertIfMissing("INSERT INTO genres (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = ?)",
                "Adventure", "Adventure");
        insertIfMissing("INSERT INTO genres (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = ?)",
                "Crime", "Crime");
    }

    private void seedMovies() {
        insertMovie("Inception", "Dreams within dreams", 2010, "MOVIE");
        insertMovie("Breaking Bad", "Drug empire story", 2008, "SERIES");
        insertMovie("The Dark Knight", "Batman vs Joker", 2008, "MOVIE");
        insertMovie("Interstellar", "Space exploration", 2014, "MOVIE");
        insertMovie("The Matrix", "Virtual reality", 1999, "MOVIE");
        insertMovie("Friends", "Group of friends", 1994, "SERIES");
        insertMovie("Gladiator", "Roman general revenge", 2000, "MOVIE");
        insertMovie("Titanic", "Ship tragedy love story", 1997, "MOVIE");
        insertMovie("The Office", "Mockumentary office life", 2005, "SERIES");
        insertMovie("Avengers", "Superhero team", 2012, "MOVIE");
        insertMovie("Pulp Fiction", "Intersecting crime stories in Los Angeles", 1994, "MOVIE");
        insertMovie("The Shawshank Redemption", "Hope and friendship inside prison walls", 1994, "MOVIE");
        insertMovie("Fight Club", "An office worker meets an anarchist soap maker", 1999, "MOVIE");
        insertMovie("Forrest Gump", "Life journey across decades of American history", 1994, "MOVIE");
        insertMovie("The Lord of the Rings: The Fellowship of the Ring", "A hobbit begins a dangerous quest", 2001, "MOVIE");
        insertMovie("Game of Thrones", "Noble families fight for the Iron Throne", 2011, "SERIES");
        insertMovie("Stranger Things", "Kids uncover supernatural events in a small town", 2016, "SERIES");
        insertMovie("The Godfather", "A mafia family struggles with power and loyalty", 1972, "MOVIE");
        insertMovie("The Silence of the Lambs", "A trainee seeks help from a brilliant killer", 1991, "MOVIE");
        insertMovie("The Social Network", "The rise of Facebook and its founders", 2010, "MOVIE");
        insertMovie("Parasite", "Class tension turns into a dangerous deception", 2019, "MOVIE");
        insertMovie("The Witcher", "A monster hunter navigates a fractured world", 2019, "SERIES");
        insertMovie("Dune", "A young heir faces destiny on a desert planet", 2021, "MOVIE");
        insertMovie("Severance", "Workers split their memories between work and life", 2022, "SERIES");
        insertMovie("The Bear", "A chef returns home to run a chaotic kitchen", 2022, "SERIES");
        insertMovie("Oppenheimer", "The story of the scientist behind the atomic bomb", 2023, "MOVIE");
        insertMovie("The Batman", "A young Batman uncovers corruption in Gotham", 2022, "MOVIE");
        insertMovie("Joker", "A troubled man descends into violence and notoriety", 2019, "MOVIE");
        insertMovie("Whiplash", "A drummer faces brutal pressure from his instructor", 2014, "MOVIE");
        insertMovie("La La Land", "Two artists fall in love while chasing ambition", 2016, "MOVIE");
        insertMovie("Blade Runner 2049", "A replicant hunter uncovers a buried secret", 2017, "MOVIE");
        insertMovie("Mad Max: Fury Road", "A road war erupts across a brutal wasteland", 2015, "MOVIE");
        insertMovie("The Prestige", "Two magicians wage a dangerous rivalry", 2006, "MOVIE");
        insertMovie("Prisoners", "A father hunts for answers after a kidnapping", 2013, "MOVIE");
        insertMovie("Chernobyl", "A disaster exposes lies inside the Soviet system", 2019, "SERIES");
        insertMovie("Dark", "A missing child unravels a time-bending mystery", 2017, "SERIES");
        insertMovie("Narcos", "The rise and fall of Pablo Escobar", 2015, "SERIES");
        insertMovie("Mindhunter", "FBI agents study serial killers to profile future crimes", 2017, "SERIES");
        insertMovie("Black Mirror", "Standalone stories explore technology gone wrong", 2011, "SERIES");
        insertMovie("The Mandalorian", "A lone bounty hunter protects a mysterious child", 2019, "SERIES");
        insertMovie("Peaky Blinders", "A gang family builds power in postwar Birmingham", 2013, "SERIES");
        insertMovie("Shutter Island", "A marshal investigates a disappearance at an asylum", 2010, "MOVIE");
        insertMovie("Arrival", "A linguist tries to communicate with alien visitors", 2016, "MOVIE");
        insertMovie("The Green Mile", "A prison guard witnesses supernatural events on death row", 1999, "MOVIE");
        insertMovie("The Truman Show", "A man discovers his whole life is a television set", 1998, "MOVIE");
    }

    private void seedMovieGenres() {
        insertMovieGenre(movie("Inception", 2010, "MOVIE"), "Sci-Fi");
        insertMovieGenre(movie("Inception", 2010, "MOVIE"), "Action");
        insertMovieGenre(movie("Breaking Bad", 2008, "SERIES"), "Drama");
        insertMovieGenre(movie("The Dark Knight", 2008, "MOVIE"), "Action");
        insertMovieGenre(movie("The Dark Knight", 2008, "MOVIE"), "Drama");
        insertMovieGenre(movie("Interstellar", 2014, "MOVIE"), "Sci-Fi");
        insertMovieGenre(movie("The Matrix", 1999, "MOVIE"), "Sci-Fi");
        insertMovieGenre(movie("Friends", 1994, "SERIES"), "Comedy");
        insertMovieGenre(movie("Gladiator", 2000, "MOVIE"), "Action");
        insertMovieGenre(movie("Titanic", 1997, "MOVIE"), "Romance");
        insertMovieGenre(movie("The Office", 2005, "SERIES"), "Comedy");
        insertMovieGenre(movie("Avengers", 2012, "MOVIE"), "Action");
        insertMovieGenre(movie("Avengers", 2012, "MOVIE"), "Adventure");
        insertMovieGenre(movie("Gladiator", 2000, "MOVIE"), "Drama");
        insertMovieGenre(movie("The Matrix", 1999, "MOVIE"), "Action");
        insertMovieGenre(movie("Pulp Fiction", 1994, "MOVIE"), "Crime");
        insertMovieGenre(movie("Pulp Fiction", 1994, "MOVIE"), "Drama");
        insertMovieGenre(movie("The Shawshank Redemption", 1994, "MOVIE"), "Drama");
        insertMovieGenre(movie("Fight Club", 1999, "MOVIE"), "Drama");
        insertMovieGenre(movie("Fight Club", 1999, "MOVIE"), "Thriller");
        insertMovieGenre(movie("Forrest Gump", 1994, "MOVIE"), "Drama");
        insertMovieGenre(movie("The Lord of the Rings: The Fellowship of the Ring", 2001, "MOVIE"), "Fantasy");
        insertMovieGenre(movie("The Lord of the Rings: The Fellowship of the Ring", 2001, "MOVIE"), "Adventure");
        insertMovieGenre(movie("Game of Thrones", 2011, "SERIES"), "Fantasy");
        insertMovieGenre(movie("Game of Thrones", 2011, "SERIES"), "Drama");
        insertMovieGenre(movie("Stranger Things", 2016, "SERIES"), "Sci-Fi");
        insertMovieGenre(movie("Stranger Things", 2016, "SERIES"), "Horror");
        insertMovieGenre(movie("The Godfather", 1972, "MOVIE"), "Crime");
        insertMovieGenre(movie("The Godfather", 1972, "MOVIE"), "Drama");
        insertMovieGenre(movie("The Silence of the Lambs", 1991, "MOVIE"), "Thriller");
        insertMovieGenre(movie("The Silence of the Lambs", 1991, "MOVIE"), "Crime");
        insertMovieGenre(movie("The Social Network", 2010, "MOVIE"), "Drama");
        insertMovieGenre(movie("Parasite", 2019, "MOVIE"), "Thriller");
        insertMovieGenre(movie("Parasite", 2019, "MOVIE"), "Drama");
        insertMovieGenre(movie("The Witcher", 2019, "SERIES"), "Fantasy");
        insertMovieGenre(movie("The Witcher", 2019, "SERIES"), "Adventure");
        insertMovieGenre(movie("Dune", 2021, "MOVIE"), "Sci-Fi");
        insertMovieGenre(movie("Dune", 2021, "MOVIE"), "Adventure");
        insertMovieGenre(movie("Severance", 2022, "SERIES"), "Sci-Fi");
        insertMovieGenre(movie("Severance", 2022, "SERIES"), "Thriller");
        insertMovieGenre(movie("The Bear", 2022, "SERIES"), "Drama");
        insertMovieGenre(movie("The Bear", 2022, "SERIES"), "Comedy");
        insertMovieGenre(movie("Oppenheimer", 2023, "MOVIE"), "Drama");
        insertMovieGenre(movie("Oppenheimer", 2023, "MOVIE"), "Thriller");
        insertMovieGenre(movie("The Batman", 2022, "MOVIE"), "Action");
        insertMovieGenre(movie("The Batman", 2022, "MOVIE"), "Crime");
        insertMovieGenre(movie("Joker", 2019, "MOVIE"), "Drama");
        insertMovieGenre(movie("Joker", 2019, "MOVIE"), "Thriller");
        insertMovieGenre(movie("Whiplash", 2014, "MOVIE"), "Drama");
        insertMovieGenre(movie("La La Land", 2016, "MOVIE"), "Romance");
        insertMovieGenre(movie("La La Land", 2016, "MOVIE"), "Drama");
        insertMovieGenre(movie("Blade Runner 2049", 2017, "MOVIE"), "Sci-Fi");
        insertMovieGenre(movie("Blade Runner 2049", 2017, "MOVIE"), "Thriller");
        insertMovieGenre(movie("Mad Max: Fury Road", 2015, "MOVIE"), "Action");
        insertMovieGenre(movie("Mad Max: Fury Road", 2015, "MOVIE"), "Adventure");
        insertMovieGenre(movie("The Prestige", 2006, "MOVIE"), "Drama");
        insertMovieGenre(movie("The Prestige", 2006, "MOVIE"), "Thriller");
        insertMovieGenre(movie("Prisoners", 2013, "MOVIE"), "Thriller");
        insertMovieGenre(movie("Prisoners", 2013, "MOVIE"), "Crime");
        insertMovieGenre(movie("Chernobyl", 2019, "SERIES"), "Drama");
        insertMovieGenre(movie("Chernobyl", 2019, "SERIES"), "Thriller");
        insertMovieGenre(movie("Dark", 2017, "SERIES"), "Sci-Fi");
        insertMovieGenre(movie("Dark", 2017, "SERIES"), "Thriller");
        insertMovieGenre(movie("Narcos", 2015, "SERIES"), "Crime");
        insertMovieGenre(movie("Narcos", 2015, "SERIES"), "Drama");
        insertMovieGenre(movie("Mindhunter", 2017, "SERIES"), "Crime");
        insertMovieGenre(movie("Mindhunter", 2017, "SERIES"), "Thriller");
        insertMovieGenre(movie("Black Mirror", 2011, "SERIES"), "Sci-Fi");
        insertMovieGenre(movie("Black Mirror", 2011, "SERIES"), "Thriller");
        insertMovieGenre(movie("The Mandalorian", 2019, "SERIES"), "Sci-Fi");
        insertMovieGenre(movie("The Mandalorian", 2019, "SERIES"), "Adventure");
        insertMovieGenre(movie("Peaky Blinders", 2013, "SERIES"), "Crime");
        insertMovieGenre(movie("Peaky Blinders", 2013, "SERIES"), "Drama");
        insertMovieGenre(movie("Shutter Island", 2010, "MOVIE"), "Thriller");
        insertMovieGenre(movie("Shutter Island", 2010, "MOVIE"), "Drama");
        insertMovieGenre(movie("Arrival", 2016, "MOVIE"), "Sci-Fi");
        insertMovieGenre(movie("Arrival", 2016, "MOVIE"), "Drama");
        insertMovieGenre(movie("The Green Mile", 1999, "MOVIE"), "Drama");
        insertMovieGenre(movie("The Green Mile", 1999, "MOVIE"), "Fantasy");
        insertMovieGenre(movie("The Truman Show", 1998, "MOVIE"), "Drama");
        insertMovieGenre(movie("The Truman Show", 1998, "MOVIE"), "Comedy");
    }

    private void seedRoles() {
        insertIfMissing("INSERT INTO roles (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = ?)",
                "ACTOR", "ACTOR");
        insertIfMissing("INSERT INTO roles (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = ?)",
                "DIRECTOR", "DIRECTOR");
        insertIfMissing("INSERT INTO roles (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = ?)",
                "WRITER", "WRITER");
        insertIfMissing("INSERT INTO roles (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = ?)",
                "PRODUCER", "PRODUCER");
        insertIfMissing("INSERT INTO roles (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = ?)",
                "CINEMATOGRAPHER", "CINEMATOGRAPHER");
    }

    private void seedPeople() {
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)",
                "Leonardo DiCaprio", "Leonardo DiCaprio");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)",
                "Christopher Nolan", "Christopher Nolan");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)",
                "Keanu Reeves", "Keanu Reeves");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)",
                "Bryan Cranston", "Bryan Cranston");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)",
                "Aaron Paul", "Aaron Paul");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)",
                "Matthew McConaughey", "Matthew McConaughey");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)",
                "Russell Crowe", "Russell Crowe");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)",
                "Kate Winslet", "Kate Winslet");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)",
                "Steve Carell", "Steve Carell");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)",
                "Robert Downey Jr.", "Robert Downey Jr.");
    }

    private void seedMovieCast() {
        insertMovieCast(movie("Inception", 2010, "MOVIE"), "Leonardo DiCaprio", "ACTOR");
        insertMovieCast(movie("Inception", 2010, "MOVIE"), "Christopher Nolan", "DIRECTOR");
        insertMovieCast(movie("The Dark Knight", 2008, "MOVIE"), "Christopher Nolan", "DIRECTOR");
        insertMovieCast(movie("The Matrix", 1999, "MOVIE"), "Keanu Reeves", "ACTOR");
        insertMovieCast(movie("Breaking Bad", 2008, "SERIES"), "Bryan Cranston", "ACTOR");
        insertMovieCast(movie("Breaking Bad", 2008, "SERIES"), "Aaron Paul", "ACTOR");
        insertMovieCast(movie("Interstellar", 2014, "MOVIE"), "Matthew McConaughey", "ACTOR");
        insertMovieCast(movie("Gladiator", 2000, "MOVIE"), "Russell Crowe", "ACTOR");
        insertMovieCast(movie("Titanic", 1997, "MOVIE"), "Kate Winslet", "ACTOR");
        insertMovieCast(movie("The Office", 2005, "SERIES"), "Steve Carell", "ACTOR");
        insertMovieCast(movie("Avengers", 2012, "MOVIE"), "Robert Downey Jr.", "ACTOR");
        insertMovieCast(movie("Interstellar", 2014, "MOVIE"), "Christopher Nolan", "DIRECTOR");
        insertMovieCast(movie("Avengers", 2012, "MOVIE"), "Christopher Nolan", "WRITER");
        insertMovieCast(movie("Gladiator", 2000, "MOVIE"), "Christopher Nolan", "WRITER");
        insertMovieCast(movie("The Matrix", 1999, "MOVIE"), "Christopher Nolan", "DIRECTOR");
    }

    private void seedRatings() {
        insertRating("user1@example.com", movie("Inception", 2010, "MOVIE"), 9);
        insertRating("user2@example.com", movie("Inception", 2010, "MOVIE"), 8);
        insertRating("user3@example.com", movie("Breaking Bad", 2008, "SERIES"), 9);
        insertRating("user4@example.com", movie("The Dark Knight", 2008, "MOVIE"), 10);
        insertRating("user5@example.com", movie("Interstellar", 2014, "MOVIE"), 9);
        insertRating("user6@example.com", movie("The Matrix", 1999, "MOVIE"), 8);
        insertRating("user7@example.com", movie("Friends", 1994, "SERIES"), 7);
        insertRating("user8@example.com", movie("Gladiator", 2000, "MOVIE"), 9);
        insertRating("user9@example.com", movie("Titanic", 1997, "MOVIE"), 8);
        insertRating("admin@example.com", movie("The Office", 2005, "SERIES"), 7);
    }

    private void seedReviews() {
        insertReview("user1@example.com", movie("Inception", 2010, "MOVIE"), "Great movie");
        insertReview("user2@example.com", movie("Inception", 2010, "MOVIE"), "Mind blowing");
        insertReview("user3@example.com", movie("Breaking Bad", 2008, "SERIES"), "Amazing series");
        insertReview("user4@example.com", movie("The Dark Knight", 2008, "MOVIE"), "Best Batman");
        insertReview("user5@example.com", movie("Interstellar", 2014, "MOVIE"), "Epic sci-fi");
        insertReview("user6@example.com", movie("The Matrix", 1999, "MOVIE"), "Classic");
        insertReview("user7@example.com", movie("Friends", 1994, "SERIES"), "Very funny");
        insertReview("user8@example.com", movie("Gladiator", 2000, "MOVIE"), "Powerful story");
        insertReview("user9@example.com", movie("Titanic", 1997, "MOVIE"), "Emotional");
        insertReview("admin@example.com", movie("The Office", 2005, "SERIES"), "Hilarious");
    }

    private void seedUserLists() {
        insertUserList("user1@example.com", "Watchlist", "WATCHLIST");
        insertUserList("user2@example.com", "Favorites", "FAVORITES");
        insertUserList("user3@example.com", "Watched", "WATCHED");
        insertUserList("user4@example.com", "My List", "CUSTOM");
        insertUserList("user5@example.com", "Sci-Fi", "CUSTOM");
        insertUserList("user6@example.com", "Top", "CUSTOM");
        insertUserList("user7@example.com", "Later", "WATCHLIST");
        insertUserList("user8@example.com", "Best", "FAVORITES");
        insertUserList("user9@example.com", "Seen", "WATCHED");
        insertUserList("admin@example.com", "Mix", "CUSTOM");
    }

    private void seedUserListItems() {
        insertUserListItem("user1@example.com", "Watchlist", movie("Inception", 2010, "MOVIE"), 1);
        insertUserListItem("user1@example.com", "Watchlist", movie("Breaking Bad", 2008, "SERIES"), 2);
        insertUserListItem("user2@example.com", "Favorites", movie("The Dark Knight", 2008, "MOVIE"), 1);
        insertUserListItem("user2@example.com", "Favorites", movie("Interstellar", 2014, "MOVIE"), 2);
        insertUserListItem("user3@example.com", "Watched", movie("The Matrix", 1999, "MOVIE"), 1);
        insertUserListItem("user4@example.com", "My List", movie("Friends", 1994, "SERIES"), 1);
        insertUserListItem("user5@example.com", "Sci-Fi", movie("Inception", 2010, "MOVIE"), 1);
        insertUserListItem("user6@example.com", "Top", movie("Gladiator", 2000, "MOVIE"), 1);
        insertUserListItem("user7@example.com", "Later", movie("Titanic", 1997, "MOVIE"), 1);
        insertUserListItem("user8@example.com", "Best", movie("The Office", 2005, "SERIES"), 1);
        insertUserListItem("user9@example.com", "Seen", movie("Avengers", 2012, "MOVIE"), 1);
        insertUserListItem("admin@example.com", "Mix", movie("Breaking Bad", 2008, "SERIES"), 1);
        insertUserListItem("user3@example.com", "Watched", movie("Interstellar", 2014, "MOVIE"), 2);
        insertUserListItem("user5@example.com", "Sci-Fi", movie("Gladiator", 2000, "MOVIE"), 2);
        insertUserListItem("user6@example.com", "Top", movie("Titanic", 1997, "MOVIE"), 2);
    }

    private void seedReports() {
        insertReport("user1@example.com", "ACTIVITY", "/r1.pdf");
        insertReport("user2@example.com", "ACTIVITY", "/r2.pdf");
        insertReport("user3@example.com", "ACTIVITY", "/r3.pdf");
        insertReport("user4@example.com", "ACTIVITY", "/r4.pdf");
        insertReport("user5@example.com", "ACTIVITY", "/r5.pdf");
        insertReport("user6@example.com", "ACTIVITY", "/r6.pdf");
        insertReport("user7@example.com", "ACTIVITY", "/r7.pdf");
        insertReport("user8@example.com", "ACTIVITY", "/r8.pdf");
        insertReport("user9@example.com", "ACTIVITY", "/r9.pdf");
        insertReport("admin@example.com", "ADMIN", "/r10.pdf");
    }

    private void upsertUser(String username, String email, String passwordHash, String role) {
        jdbcTemplate.update("""
                INSERT INTO users (username, email, password_hash, role, is_blocked)
                VALUES (?, ?, ?, ?, FALSE)
                ON CONFLICT (email) DO UPDATE SET
                    username = EXCLUDED.username,
                    password_hash = EXCLUDED.password_hash,
                    role = EXCLUDED.role
                """, username, email, passwordHash, role);
    }

    private SeedMovieRef movie(String title, int releaseYear, String type) {
        return new SeedMovieRef(title, releaseYear, type);
    }

    private void insertMovie(String title, String description, int releaseYear, String type) {
        jdbcTemplate.update("""
                INSERT INTO movies (title, description, release_year, type)
                VALUES (?, ?, ?, ?)
                ON CONFLICT (title, release_year, type) DO UPDATE SET
                    description = EXCLUDED.description
                """, title, description, releaseYear, type);
    }

    private void insertMovieGenre(SeedMovieRef movieRef, String genreName) {
        insertIfMissing("""
                INSERT INTO movie_genres (movie_id, genre_id)
                SELECT m.id, g.id
                FROM movies m
                JOIN genres g ON g.name = ?
                WHERE m.title = ?
                  AND m.release_year = ?
                  AND m.type = ?
                  AND NOT EXISTS (
                      SELECT 1
                      FROM movie_genres mg
                      WHERE mg.movie_id = m.id AND mg.genre_id = g.id
                  )
                """, genreName, movieRef.title(), movieRef.releaseYear(), movieRef.type());
    }

    private void insertMovieCast(SeedMovieRef movieRef, String personName, String roleName) {
        insertIfMissing("""
                INSERT INTO movie_cast (movie_id, person_id, role_id)
                SELECT m.id, p.id, r.id
                FROM movies m
                JOIN people p ON p.name = ?
                JOIN roles r ON r.name = ?
                WHERE m.title = ?
                  AND m.release_year = ?
                  AND m.type = ?
                  AND NOT EXISTS (
                      SELECT 1
                      FROM movie_cast mc
                      WHERE mc.movie_id = m.id AND mc.person_id = p.id AND mc.role_id = r.id
                  )
                """, personName, roleName, movieRef.title(), movieRef.releaseYear(), movieRef.type());
    }

    private void insertRating(String userEmail, SeedMovieRef movieRef, int rating) {
        insertIfMissing("""
                INSERT INTO ratings (user_id, movie_id, rating)
                SELECT u.id, m.id, ?
                FROM users u
                JOIN movies m ON m.title = ?
                    AND m.release_year = ?
                    AND m.type = ?
                WHERE u.email = ?
                  AND NOT EXISTS (
                      SELECT 1
                      FROM ratings r
                      WHERE r.user_id = u.id AND r.movie_id = m.id
                  )
                """, rating, movieRef.title(), movieRef.releaseYear(), movieRef.type(), userEmail);
    }

    private void insertReview(String userEmail, SeedMovieRef movieRef, String content) {
        insertIfMissing("""
                INSERT INTO reviews (user_id, movie_id, content)
                SELECT u.id, m.id, ?
                FROM users u
                JOIN movies m ON m.title = ?
                    AND m.release_year = ?
                    AND m.type = ?
                WHERE u.email = ?
                  AND NOT EXISTS (
                      SELECT 1
                      FROM reviews r
                      WHERE r.user_id = u.id AND r.movie_id = m.id AND r.content = ?
                  )
                """, content, movieRef.title(), movieRef.releaseYear(), movieRef.type(), userEmail, content);
    }

    private void insertUserList(String userEmail, String listName, String type) {
        insertIfMissing("""
                INSERT INTO user_lists (user_id, name, type)
                SELECT u.id, ?, ?
                FROM users u
                WHERE u.email = ?
                  AND NOT EXISTS (
                      SELECT 1
                      FROM user_lists ul
                      WHERE ul.user_id = u.id AND ul.name = ?
                  )
                """, listName, type, userEmail, listName);
    }

    private void insertUserListItem(String userEmail, String listName, SeedMovieRef movieRef, int position) {
        insertIfMissing("""
                INSERT INTO user_list_items (list_id, movie_id, position)
                SELECT ul.id, m.id, ?
                FROM user_lists ul
                JOIN users u ON u.id = ul.user_id
                JOIN movies m ON m.title = ?
                    AND m.release_year = ?
                    AND m.type = ?
                WHERE ul.name = ?
                  AND u.email = ?
                  AND NOT EXISTS (
                      SELECT 1
                      FROM user_list_items uli
                      WHERE uli.list_id = ul.id AND uli.movie_id = m.id
                  )
                """, position, movieRef.title(), movieRef.releaseYear(), movieRef.type(), listName, userEmail);
    }

    private void insertReport(String userEmail, String type, String filePath) {
        insertIfMissing("""
                INSERT INTO reports (user_id, type, file_path)
                SELECT u.id, ?, ?
                FROM users u
                WHERE u.email = ?
                  AND NOT EXISTS (
                      SELECT 1
                      FROM reports r
                      WHERE r.user_id = u.id AND r.file_path = ?
                  )
                """, type, filePath, userEmail, filePath);
    }

    private void insertIfMissing(String sql, Object... args) {
        jdbcTemplate.update(sql, args);
    }
}
