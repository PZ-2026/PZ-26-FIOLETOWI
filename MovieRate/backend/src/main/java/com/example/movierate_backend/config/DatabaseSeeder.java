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
        insertIfMissing("INSERT INTO genres (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = ?)", "Action", "Action");
        insertIfMissing("INSERT INTO genres (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = ?)", "Drama", "Drama");
        insertIfMissing("INSERT INTO genres (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = ?)", "Comedy", "Comedy");
        insertIfMissing("INSERT INTO genres (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = ?)", "Sci-Fi", "Sci-Fi");
        insertIfMissing("INSERT INTO genres (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = ?)", "Horror", "Horror");
        insertIfMissing("INSERT INTO genres (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = ?)", "Thriller", "Thriller");
        insertIfMissing("INSERT INTO genres (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = ?)", "Romance", "Romance");
        insertIfMissing("INSERT INTO genres (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = ?)", "Fantasy", "Fantasy");
        insertIfMissing("INSERT INTO genres (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = ?)", "Adventure", "Adventure");
        insertIfMissing("INSERT INTO genres (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = ?)", "Crime", "Crime");
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
        insertIfMissing("INSERT INTO roles (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = ?)", "ACTOR", "ACTOR");
        insertIfMissing("INSERT INTO roles (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = ?)", "DIRECTOR", "DIRECTOR");
        insertIfMissing("INSERT INTO roles (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = ?)", "WRITER", "WRITER");
        insertIfMissing("INSERT INTO roles (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = ?)", "PRODUCER", "PRODUCER");
        insertIfMissing("INSERT INTO roles (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = ?)", "CINEMATOGRAPHER", "CINEMATOGRAPHER");
    }

    private void seedPeople() {
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Leonardo DiCaprio", "Leonardo DiCaprio");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Christopher Nolan", "Christopher Nolan");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Keanu Reeves", "Keanu Reeves");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Bryan Cranston", "Bryan Cranston");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Aaron Paul", "Aaron Paul");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Matthew McConaughey", "Matthew McConaughey");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Russell Crowe", "Russell Crowe");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Kate Winslet", "Kate Winslet");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Steve Carell", "Steve Carell");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Robert Downey Jr.", "Robert Downey Jr.");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Christian Bale", "Christian Bale");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Heath Ledger", "Heath Ledger");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Carrie-Anne Moss", "Carrie-Anne Moss");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Jennifer Aniston", "Jennifer Aniston");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "David Schwimmer", "David Schwimmer");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Joaquin Phoenix", "Joaquin Phoenix");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Tom Hanks", "Tom Hanks");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Tim Robbins", "Tim Robbins");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Morgan Freeman", "Morgan Freeman");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Brad Pitt", "Brad Pitt");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Edward Norton", "Edward Norton");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Robin Wright", "Robin Wright");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Elijah Wood", "Elijah Wood");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Ian McKellen", "Ian McKellen");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Peter Jackson", "Peter Jackson");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Emilia Clarke", "Emilia Clarke");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Kit Harington", "Kit Harington");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "David Benioff", "David Benioff");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Millie Bobby Brown", "Millie Bobby Brown");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Winona Ryder", "Winona Ryder");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Marlon Brando", "Marlon Brando");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Al Pacino", "Al Pacino");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Francis Ford Coppola", "Francis Ford Coppola");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Jodie Foster", "Jodie Foster");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Anthony Hopkins", "Anthony Hopkins");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Jonathan Demme", "Jonathan Demme");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Jesse Eisenberg", "Jesse Eisenberg");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "David Fincher", "David Fincher");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Song Kang-ho", "Song Kang-ho");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Bong Joon-ho", "Bong Joon-ho");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Henry Cavill", "Henry Cavill");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Anya Chalotra", "Anya Chalotra");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Timothee Chalamet", "Timothee Chalamet");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Zendaya", "Zendaya");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Denis Villeneuve", "Denis Villeneuve");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Adam Scott", "Adam Scott");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Patricia Arquette", "Patricia Arquette");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Jeremy Allen White", "Jeremy Allen White");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Ebon Moss-Bachrach", "Ebon Moss-Bachrach");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Cillian Murphy", "Cillian Murphy");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Robert Pattinson", "Robert Pattinson");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Zoe Kravitz", "Zoe Kravitz");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Matt Reeves", "Matt Reeves");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Miles Teller", "Miles Teller");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "J.K. Simmons", "J.K. Simmons");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Damien Chazelle", "Damien Chazelle");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Ryan Gosling", "Ryan Gosling");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Emma Stone", "Emma Stone");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Harrison Ford", "Harrison Ford");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Tom Hardy", "Tom Hardy");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Charlize Theron", "Charlize Theron");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "George Miller", "George Miller");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Hugh Jackman", "Hugh Jackman");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Jake Gyllenhaal", "Jake Gyllenhaal");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Jared Harris", "Jared Harris");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Stellan Skarsgard", "Stellan Skarsgard");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Louis Hofmann", "Louis Hofmann");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Wagner Moura", "Wagner Moura");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Pedro Pascal", "Pedro Pascal");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Jonathan Groff", "Jonathan Groff");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Holt McCallany", "Holt McCallany");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Amy Adams", "Amy Adams");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Jeremy Renner", "Jeremy Renner");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Michael Caine", "Michael Caine");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Gary Oldman", "Gary Oldman");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Sam Worthington", "Sam Worthington");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Zoe Saldana", "Zoe Saldana");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "James Cameron", "James Cameron");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Margot Robbie", "Margot Robbie");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Quentin Tarantino", "Quentin Tarantino");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Samuel L. Jackson", "Samuel L. Jackson");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "John Travolta", "John Travolta");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Uma Thurman", "Uma Thurman");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Frank Darabont", "Frank Darabont");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Bob Odenkirk", "Bob Odenkirk");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Giancarlo Esposito", "Giancarlo Esposito");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Vince Gilligan", "Vince Gilligan");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Courteney Cox", "Courteney Cox");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Lisa Kudrow", "Lisa Kudrow");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Matt LeBlanc", "Matt LeBlanc");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Matthew Perry", "Matthew Perry");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Ridley Scott", "Ridley Scott");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Joseph Gordon-Levitt", "Joseph Gordon-Levitt");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Elliot Page", "Elliot Page");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Ken Watanabe", "Ken Watanabe");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Marion Cotillard", "Marion Cotillard");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Anne Hathaway", "Anne Hathaway");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Jessica Chastain", "Jessica Chastain");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Mackenzie Foy", "Mackenzie Foy");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Laurence Fishburne", "Laurence Fishburne");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Hugo Weaving", "Hugo Weaving");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Lana Wachowski", "Lana Wachowski");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Lilly Wachowski", "Lilly Wachowski");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Viggo Mortensen", "Viggo Mortensen");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Sean Astin", "Sean Astin");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Fran Walsh", "Fran Walsh");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Philippa Boyens", "Philippa Boyens");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Lena Headey", "Lena Headey");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Peter Dinklage", "Peter Dinklage");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "D.B. Weiss", "D.B. Weiss");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "George R.R. Martin", "George R.R. Martin");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Finn Wolfhard", "Finn Wolfhard");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "David Harbour", "David Harbour");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "The Duffer Brothers", "The Duffer Brothers");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "James Gandolfini", "James Gandolfini");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Liam Neeson", "Liam Neeson");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Ralph Fiennes", "Ralph Fiennes");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Ben Kingsley", "Ben Kingsley");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Steven Spielberg", "Steven Spielberg");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Matt Damon", "Matt Damon");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Mark Wahlberg", "Mark Wahlberg");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Sally Field", "Sally Field");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Robert Zemeckis", "Robert Zemeckis");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Eric Roth", "Eric Roth");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Winston Groom", "Winston Groom");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "David Lynch", "David Lynch");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Kyle MacLachlan", "Kyle MacLachlan");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Laura Dern", "Laura Dern");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Mark Hamill", "Mark Hamill");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Daisy Ridley", "Daisy Ridley");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "John Boyega", "John Boyega");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Oscar Isaac", "Oscar Isaac");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Adam Driver", "Adam Driver");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "J.J. Abrams", "J.J. Abrams");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Chris Evans", "Chris Evans");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Chris Hemsworth", "Chris Hemsworth");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Mark Ruffalo", "Mark Ruffalo");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Scarlett Johansson", "Scarlett Johansson");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Joss Whedon", "Joss Whedon");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Zak Penn", "Zak Penn");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "David S. Goyer", "David S. Goyer");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Jonathan Nolan", "Jonathan Nolan");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "David Cronenberg", "David Cronenberg");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Vivien Leigh", "Vivien Leigh");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Clark Gable", "Clark Gable");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Victor Fleming", "Victor Fleming");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Sidney Howard", "Sidney Howard");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Margaret Mitchell", "Margaret Mitchell");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Paul Thomas Anderson", "Paul Thomas Anderson");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Daniel Day-Lewis", "Daniel Day-Lewis");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Martin Scorsese", "Martin Scorsese");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Joe Pesci", "Joe Pesci");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Robert De Niro", "Robert De Niro");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Sharon Stone", "Sharon Stone");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Michael Mann", "Michael Mann");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Val Kilmer", "Val Kilmer");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Jon Voight", "Jon Voight");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Tom Cruise", "Tom Cruise");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Dustin Hoffman", "Dustin Hoffman");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Barry Levinson", "Barry Levinson");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Philip Seymour Hoffman", "Philip Seymour Hoffman");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "John C. Reilly", "John C. Reilly");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Paul Dano", "Paul Dano");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Woody Harrelson", "Woody Harrelson");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Jared Leto", "Jared Leto");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Ellen Burstyn", "Ellen Burstyn");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Jennifer Connelly", "Jennifer Connelly");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Mickey Rourke", "Mickey Rourke");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Darren Aronofsky", "Darren Aronofsky");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Natalie Portman", "Natalie Portman");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Mila Kunis", "Mila Kunis");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Vincent Cassel", "Vincent Cassel");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Clint Eastwood", "Clint Eastwood");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Hilary Swank", "Hilary Swank");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Paul Haggis", "Paul Haggis");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "F. Murray Abraham", "F. Murray Abraham");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Tom Hulce", "Tom Hulce");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Milos Forman", "Milos Forman");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Peter Shaffer", "Peter Shaffer");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Jack Nicholson", "Jack Nicholson");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Shelley Duvall", "Shelley Duvall");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Stanley Kubrick", "Stanley Kubrick");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Diane Johnson", "Diane Johnson");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Stephen King", "Stephen King");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Michael Douglas", "Michael Douglas");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Glenn Close", "Glenn Close");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Adrian Lyne", "Adrian Lyne");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "James Dearden", "James Dearden");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Demi Moore", "Demi Moore");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Whoopi Goldberg", "Whoopi Goldberg");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Jerry Zucker", "Jerry Zucker");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Bruce Joel Rubin", "Bruce Joel Rubin");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Patrick Swayze", "Patrick Swayze");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Demi Moore", "Demi Moore");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Tony Scott", "Tony Scott");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Jim Cash", "Jim Cash");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Jack Epps Jr.", "Jack Epps Jr.");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Tim Burton", "Tim Burton");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Johnny Depp", "Johnny Depp");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Helena Bonham Carter", "Helena Bonham Carter");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Alan Rickman", "Alan Rickman");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Linda Woolverton", "Linda Woolverton");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Sam Raimi", "Sam Raimi");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Tobey Maguire", "Tobey Maguire");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Kirsten Dunst", "Kirsten Dunst");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Willem Dafoe", "Willem Dafoe");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "David Koepp", "David Koepp");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Alfonso Cuaron", "Alfonso Cuaron");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Sandra Bullock", "Sandra Bullock");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "George Clooney", "George Clooney");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Jonas Cuaron", "Jonas Cuaron");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Ed Harris", "Ed Harris");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Bill Paxton", "Bill Paxton");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Kathryn Bigelow", "Kathryn Bigelow");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Mark Boal", "Mark Boal");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Jeremy Renner", "Jeremy Renner");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Anthony Mackie", "Anthony Mackie");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Brian Geraghty", "Brian Geraghty");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Ridley Scott", "Ridley Scott");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Josh Hartnett", "Josh Hartnett");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Ewan McGregor", "Ewan McGregor");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Tom Sizemore", "Tom Sizemore");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Ken Nolan", "Ken Nolan");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Mark Bowden", "Mark Bowden");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Spike Jonze", "Spike Jonze");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Nicolas Cage", "Nicolas Cage");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Meryl Streep", "Meryl Streep");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Chris Cooper", "Chris Cooper");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Charlie Kaufman", "Charlie Kaufman");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Susan Orlean", "Susan Orlean");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Wes Anderson", "Wes Anderson");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Bill Murray", "Bill Murray");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Owen Wilson", "Owen Wilson");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Adrien Brody", "Adrien Brody");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Roman Coppola", "Roman Coppola");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Jason Schwartzman", "Jason Schwartzman");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Ang Lee", "Ang Lee");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Jake Gyllenhaal", "Jake Gyllenhaal");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Michelle Williams", "Michelle Williams");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Larry McMurtry", "Larry McMurtry");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Diana Ossana", "Diana Ossana");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Annie Proulx", "Annie Proulx");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Heath Ledger", "Heath Ledger");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Randy Quaid", "Randy Quaid");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "John Cusack", "John Cusack");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Thora Birch", "Thora Birch");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Annette Bening", "Annette Bening");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Sam Mendes", "Sam Mendes");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Alan Ball", "Alan Ball");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Justin Lin", "Justin Lin");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Vin Diesel", "Vin Diesel");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Paul Walker", "Paul Walker");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Michelle Rodriguez", "Michelle Rodriguez");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Jordana Brewster", "Jordana Brewster");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Chris Morgan", "Chris Morgan");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Gary Scott Thompson", "Gary Scott Thompson");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Zach Braff", "Zach Braff");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Donald Faison", "Donald Faison");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Sarah Chalke", "Sarah Chalke");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Judy Reyes", "Judy Reyes");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Bill Lawrence", "Bill Lawrence");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Neil Flynn", "Neil Flynn");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Ken Jeong", "Ken Jeong");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Joel McHale", "Joel McHale");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Danny Pudi", "Danny Pudi");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Alison Brie", "Alison Brie");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Gillian Jacobs", "Gillian Jacobs");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Dan Harmon", "Dan Harmon");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Donald Glover", "Donald Glover");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Yvette Nicole Brown", "Yvette Nicole Brown");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Jim Parsons", "Jim Parsons");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Johnny Galecki", "Johnny Galecki");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Kaley Cuoco", "Kaley Cuoco");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Simon Helberg", "Simon Helberg");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Kunal Nayyar", "Kunal Nayyar");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Chuck Lorre", "Chuck Lorre");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Bill Prady", "Bill Prady");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Mayim Bialik", "Mayim Bialik");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Melissa Rauch", "Melissa Rauch");
        // Missing people referenced in seedMovieCast()
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Oliver Reed", "Oliver Reed");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Richard Harris", "Richard Harris");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Billy Zane", "Billy Zane");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Kathy Bates", "Kathy Bates");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "John Krasinski", "John Krasinski");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Jenna Fischer", "Jenna Fischer");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Rainn Wilson", "Rainn Wilson");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Tom Hiddleston", "Tom Hiddleston");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Bruce Willis", "Bruce Willis");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Tim Roth", "Tim Roth");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Amanda Plummer", "Amanda Plummer");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Bob Gunton", "Bob Gunton");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Clancy Brown", "Clancy Brown");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Meat Loaf", "Meat Loaf");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Gary Sinise", "Gary Sinise");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Mykelti Williamson", "Mykelti Williamson");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Cate Blanchett", "Cate Blanchett");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "James Caan", "James Caan");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Robert Duvall", "Robert Duvall");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Lawrence A. Bonney", "Lawrence A. Bonney");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Aaron Sorkin", "Aaron Sorkin");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Andrew Garfield", "Andrew Garfield");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Justin Timberlake", "Justin Timberlake");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Lee Sun-kyun", "Lee Sun-kyun");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Cho Yeo-jeong", "Cho Yeo-jeong");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Choi Woo-shik", "Choi Woo-shik");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Park So-dam", "Park So-dam");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Rebecca Ferguson", "Rebecca Ferguson");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Josh Brolin", "Josh Brolin");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Todd Phillips", "Todd Phillips");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Zazie Beetz", "Zazie Beetz");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Frances Conroy", "Frances Conroy");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Melissa Benoist", "Melissa Benoist");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "John Legend", "John Legend");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Ana de Armas", "Ana de Armas");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Nicholas Hoult", "Nicholas Hoult");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "David Bowie", "David Bowie");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Viola Davis", "Viola Davis");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Melissa Leo", "Melissa Leo");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Jon Hamm", "Jon Hamm");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Emily Mortimer", "Emily Mortimer");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Forest Whitaker", "Forest Whitaker");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Michael Stuhlbarg", "Michael Stuhlbarg");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Michael Clarke Duncan", "Michael Clarke Duncan");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "David Morse", "David Morse");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Bonnie Hunt", "Bonnie Hunt");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Peter Weir", "Peter Weir");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Andrew Niccol", "Andrew Niccol");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Jim Carrey", "Jim Carrey");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Laura Linney", "Laura Linney");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Noah Emmerich", "Noah Emmerich");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Colin Farrell", "Colin Farrell");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Jeffrey Wright", "Jeffrey Wright");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Emily Blunt", "Emily Blunt");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Florence Pugh", "Florence Pugh");
        // Directors and additional actors for series
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "David Crane", "David Crane");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Marta Kauffman", "Marta Kauffman");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Greg Daniels", "Greg Daniels");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Alik Sakharov", "Alik Sakharov");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Ben Stiller", "Ben Stiller");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Christopher Storer", "Christopher Storer");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Johan Renck", "Johan Renck");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Baran bo Odar", "Baran bo Odar");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Jose Padilha", "Jose Padilha");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Charlie Brooker", "Charlie Brooker");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Jon Favreau", "Jon Favreau");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Otto Bathurst", "Otto Bathurst");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Timothy Olyphant", "Timothy Olyphant");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Carl Weathers", "Carl Weathers");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Giancarlo Esposito", "Giancarlo Esposito");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Helen Paul", "Helen Paul");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Paul Anderson", "Paul Anderson");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Sophie Rundle", "Sophie Rundle");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Joe Cole", "Joe Cole");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Freya Allan", "Freya Allan");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Tramell Tillman", "Tramell Tillman");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Britt Lower", "Britt Lower");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Zach Cherry", "Zach Cherry");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Ayo Edebiri", "Ayo Edebiri");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Liza Colon-Zayas", "Liza Colon-Zayas");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Matthias Schweighofer", "Matthias Schweighofer");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Oliver Masucci", "Oliver Masucci");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Karoline Eichhorn", "Karoline Eichhorn");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Boyd Holbrook", "Boyd Holbrook");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Dami Alcazar", "Dami Alcazar");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Anna Torv", "Anna Torv");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Michael C. Hall", "Michael C. Hall");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Daniel Radcliffe", "Daniel Radcliffe");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Emma Watson", "Emma Watson");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Rupert Grint", "Rupert Grint");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Chris Columbus", "Chris Columbus");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Steve Kloves", "Steve Kloves");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "J.K. Rowling", "J.K. Rowling");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Mads Mikkelsen", "Mads Mikkelsen");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Hugh Grant", "Hugh Grant");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Sarah Jessica Parker", "Sarah Jessica Parker");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Kim Cattrall", "Kim Cattrall");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Cynthia Nixon", "Cynthia Nixon");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Kristin Davis", "Kristin Davis");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Darren Star", "Darren Star");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Michael Patrick King", "Michael Patrick King");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Julie Andrews", "Julie Andrews");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Christopher Plummer", "Christopher Plummer");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Robert Wise", "Robert Wise");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Ernest Lehman", "Ernest Lehman");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Howard Lindsay", "Howard Lindsay");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Russel Crouse", "Russel Crouse");
        insertIfMissing("INSERT INTO people (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM people WHERE name = ?)", "Maria von Trapp", "Maria von Trapp");
    }

    private void seedMovieCast() {
        // Inception (1)
        insertMovieCast(movie("Inception", 2010, "MOVIE"), "Christopher Nolan", "DIRECTOR");
        insertMovieCast(movie("Inception", 2010, "MOVIE"), "Christopher Nolan", "WRITER");
        insertMovieCast(movie("Inception", 2010, "MOVIE"), "Leonardo DiCaprio", "ACTOR");
        insertMovieCast(movie("Inception", 2010, "MOVIE"), "Joseph Gordon-Levitt", "ACTOR");
        insertMovieCast(movie("Inception", 2010, "MOVIE"), "Elliot Page", "ACTOR");
        insertMovieCast(movie("Inception", 2010, "MOVIE"), "Tom Hardy", "ACTOR");
        insertMovieCast(movie("Inception", 2010, "MOVIE"), "Ken Watanabe", "ACTOR");
        insertMovieCast(movie("Inception", 2010, "MOVIE"), "Marion Cotillard", "ACTOR");
        insertMovieCast(movie("Inception", 2010, "MOVIE"), "Cillian Murphy", "ACTOR");
        insertMovieCast(movie("Inception", 2010, "MOVIE"), "Michael Caine", "ACTOR");

        // Breaking Bad (2)
        insertMovieCast(movie("Breaking Bad", 2008, "SERIES"), "Vince Gilligan", "DIRECTOR");
        insertMovieCast(movie("Breaking Bad", 2008, "SERIES"), "Vince Gilligan", "WRITER");
        insertMovieCast(movie("Breaking Bad", 2008, "SERIES"), "Bryan Cranston", "ACTOR");
        insertMovieCast(movie("Breaking Bad", 2008, "SERIES"), "Aaron Paul", "ACTOR");
        insertMovieCast(movie("Breaking Bad", 2008, "SERIES"), "Bob Odenkirk", "ACTOR");
        insertMovieCast(movie("Breaking Bad", 2008, "SERIES"), "Giancarlo Esposito", "ACTOR");

        // The Dark Knight (3)
        insertMovieCast(movie("The Dark Knight", 2008, "MOVIE"), "Christopher Nolan", "DIRECTOR");
        insertMovieCast(movie("The Dark Knight", 2008, "MOVIE"), "Christopher Nolan", "WRITER");
        insertMovieCast(movie("The Dark Knight", 2008, "MOVIE"), "Jonathan Nolan", "WRITER");
        insertMovieCast(movie("The Dark Knight", 2008, "MOVIE"), "Christian Bale", "ACTOR");
        insertMovieCast(movie("The Dark Knight", 2008, "MOVIE"), "Heath Ledger", "ACTOR");
        insertMovieCast(movie("The Dark Knight", 2008, "MOVIE"), "Michael Caine", "ACTOR");
        insertMovieCast(movie("The Dark Knight", 2008, "MOVIE"), "Gary Oldman", "ACTOR");
        insertMovieCast(movie("The Dark Knight", 2008, "MOVIE"), "Morgan Freeman", "ACTOR");
        insertMovieCast(movie("The Dark Knight", 2008, "MOVIE"), "Cillian Murphy", "ACTOR");

        // Interstellar (4)
        insertMovieCast(movie("Interstellar", 2014, "MOVIE"), "Christopher Nolan", "DIRECTOR");
        insertMovieCast(movie("Interstellar", 2014, "MOVIE"), "Jonathan Nolan", "WRITER");
        insertMovieCast(movie("Interstellar", 2014, "MOVIE"), "Matthew McConaughey", "ACTOR");
        insertMovieCast(movie("Interstellar", 2014, "MOVIE"), "Anne Hathaway", "ACTOR");
        insertMovieCast(movie("Interstellar", 2014, "MOVIE"), "Jessica Chastain", "ACTOR");
        insertMovieCast(movie("Interstellar", 2014, "MOVIE"), "Michael Caine", "ACTOR");
        insertMovieCast(movie("Interstellar", 2014, "MOVIE"), "Mackenzie Foy", "ACTOR");
        insertMovieCast(movie("Interstellar", 2014, "MOVIE"), "Tom Hardy", "ACTOR");

        // The Matrix (5)
        insertMovieCast(movie("The Matrix", 1999, "MOVIE"), "Lana Wachowski", "DIRECTOR");
        insertMovieCast(movie("The Matrix", 1999, "MOVIE"), "Lilly Wachowski", "DIRECTOR");
        insertMovieCast(movie("The Matrix", 1999, "MOVIE"), "Lana Wachowski", "WRITER");
        insertMovieCast(movie("The Matrix", 1999, "MOVIE"), "Lilly Wachowski", "WRITER");
        insertMovieCast(movie("The Matrix", 1999, "MOVIE"), "Keanu Reeves", "ACTOR");
        insertMovieCast(movie("The Matrix", 1999, "MOVIE"), "Laurence Fishburne", "ACTOR");
        insertMovieCast(movie("The Matrix", 1999, "MOVIE"), "Carrie-Anne Moss", "ACTOR");
        insertMovieCast(movie("The Matrix", 1999, "MOVIE"), "Hugo Weaving", "ACTOR");

        // Friends (6)
        insertMovieCast(movie("Friends", 1994, "SERIES"), "David Crane", "DIRECTOR");
        insertMovieCast(movie("Friends", 1994, "SERIES"), "Marta Kauffman", "DIRECTOR");
        insertMovieCast(movie("Friends", 1994, "SERIES"), "David Crane", "WRITER");
        insertMovieCast(movie("Friends", 1994, "SERIES"), "Marta Kauffman", "WRITER");
        insertMovieCast(movie("Friends", 1994, "SERIES"), "David Schwimmer", "ACTOR");
        insertMovieCast(movie("Friends", 1994, "SERIES"), "Jennifer Aniston", "ACTOR");
        insertMovieCast(movie("Friends", 1994, "SERIES"), "Courteney Cox", "ACTOR");
        insertMovieCast(movie("Friends", 1994, "SERIES"), "Lisa Kudrow", "ACTOR");
        insertMovieCast(movie("Friends", 1994, "SERIES"), "Matt LeBlanc", "ACTOR");
        insertMovieCast(movie("Friends", 1994, "SERIES"), "Matthew Perry", "ACTOR");

        // Gladiator (7)
        insertMovieCast(movie("Gladiator", 2000, "MOVIE"), "Ridley Scott", "DIRECTOR");
        insertMovieCast(movie("Gladiator", 2000, "MOVIE"), "Russell Crowe", "ACTOR");
        insertMovieCast(movie("Gladiator", 2000, "MOVIE"), "Joaquin Phoenix", "ACTOR");
        insertMovieCast(movie("Gladiator", 2000, "MOVIE"), "Oliver Reed", "ACTOR");
        insertMovieCast(movie("Gladiator", 2000, "MOVIE"), "Richard Harris", "ACTOR");

        // Titanic (8)
        insertMovieCast(movie("Titanic", 1997, "MOVIE"), "James Cameron", "DIRECTOR");
        insertMovieCast(movie("Titanic", 1997, "MOVIE"), "James Cameron", "WRITER");
        insertMovieCast(movie("Titanic", 1997, "MOVIE"), "Leonardo DiCaprio", "ACTOR");
        insertMovieCast(movie("Titanic", 1997, "MOVIE"), "Kate Winslet", "ACTOR");
        insertMovieCast(movie("Titanic", 1997, "MOVIE"), "Billy Zane", "ACTOR");
        insertMovieCast(movie("Titanic", 1997, "MOVIE"), "Kathy Bates", "ACTOR");

        // The Office (9)
        insertMovieCast(movie("The Office", 2005, "SERIES"), "Greg Daniels", "DIRECTOR");
        insertMovieCast(movie("The Office", 2005, "SERIES"), "Greg Daniels", "WRITER");
        insertMovieCast(movie("The Office", 2005, "SERIES"), "Steve Carell", "ACTOR");
        insertMovieCast(movie("The Office", 2005, "SERIES"), "John Krasinski", "ACTOR");
        insertMovieCast(movie("The Office", 2005, "SERIES"), "Jenna Fischer", "ACTOR");
        insertMovieCast(movie("The Office", 2005, "SERIES"), "Rainn Wilson", "ACTOR");

        // Avengers (10)
        insertMovieCast(movie("Avengers", 2012, "MOVIE"), "Joss Whedon", "DIRECTOR");
        insertMovieCast(movie("Avengers", 2012, "MOVIE"), "Joss Whedon", "WRITER");
        insertMovieCast(movie("Avengers", 2012, "MOVIE"), "Robert Downey Jr.", "ACTOR");
        insertMovieCast(movie("Avengers", 2012, "MOVIE"), "Chris Evans", "ACTOR");
        insertMovieCast(movie("Avengers", 2012, "MOVIE"), "Chris Hemsworth", "ACTOR");
        insertMovieCast(movie("Avengers", 2012, "MOVIE"), "Mark Ruffalo", "ACTOR");
        insertMovieCast(movie("Avengers", 2012, "MOVIE"), "Scarlett Johansson", "ACTOR");
        insertMovieCast(movie("Avengers", 2012, "MOVIE"), "Jeremy Renner", "ACTOR");
        insertMovieCast(movie("Avengers", 2012, "MOVIE"), "Tom Hiddleston", "ACTOR");
        insertMovieCast(movie("Avengers", 2012, "MOVIE"), "Samuel L. Jackson", "ACTOR");

        // Pulp Fiction (11)
        insertMovieCast(movie("Pulp Fiction", 1994, "MOVIE"), "Quentin Tarantino", "DIRECTOR");
        insertMovieCast(movie("Pulp Fiction", 1994, "MOVIE"), "Quentin Tarantino", "WRITER");
        insertMovieCast(movie("Pulp Fiction", 1994, "MOVIE"), "John Travolta", "ACTOR");
        insertMovieCast(movie("Pulp Fiction", 1994, "MOVIE"), "Samuel L. Jackson", "ACTOR");
        insertMovieCast(movie("Pulp Fiction", 1994, "MOVIE"), "Uma Thurman", "ACTOR");
        insertMovieCast(movie("Pulp Fiction", 1994, "MOVIE"), "Bruce Willis", "ACTOR");
        insertMovieCast(movie("Pulp Fiction", 1994, "MOVIE"), "Tim Roth", "ACTOR");
        insertMovieCast(movie("Pulp Fiction", 1994, "MOVIE"), "Amanda Plummer", "ACTOR");

        // The Shawshank Redemption (12)
        insertMovieCast(movie("The Shawshank Redemption", 1994, "MOVIE"), "Frank Darabont", "DIRECTOR");
        insertMovieCast(movie("The Shawshank Redemption", 1994, "MOVIE"), "Frank Darabont", "WRITER");
        insertMovieCast(movie("The Shawshank Redemption", 1994, "MOVIE"), "Tim Robbins", "ACTOR");
        insertMovieCast(movie("The Shawshank Redemption", 1994, "MOVIE"), "Morgan Freeman", "ACTOR");
        insertMovieCast(movie("The Shawshank Redemption", 1994, "MOVIE"), "Bob Gunton", "ACTOR");
        insertMovieCast(movie("The Shawshank Redemption", 1994, "MOVIE"), "Clancy Brown", "ACTOR");

        // Fight Club (13)
        insertMovieCast(movie("Fight Club", 1999, "MOVIE"), "David Fincher", "DIRECTOR");
        insertMovieCast(movie("Fight Club", 1999, "MOVIE"), "Brad Pitt", "ACTOR");
        insertMovieCast(movie("Fight Club", 1999, "MOVIE"), "Edward Norton", "ACTOR");
        insertMovieCast(movie("Fight Club", 1999, "MOVIE"), "Helena Bonham Carter", "ACTOR");
        insertMovieCast(movie("Fight Club", 1999, "MOVIE"), "Meat Loaf", "ACTOR");
        insertMovieCast(movie("Fight Club", 1999, "MOVIE"), "Jared Leto", "ACTOR");

        // Forrest Gump (14)
        insertMovieCast(movie("Forrest Gump", 1994, "MOVIE"), "Robert Zemeckis", "DIRECTOR");
        insertMovieCast(movie("Forrest Gump", 1994, "MOVIE"), "Eric Roth", "WRITER");
        insertMovieCast(movie("Forrest Gump", 1994, "MOVIE"), "Tom Hanks", "ACTOR");
        insertMovieCast(movie("Forrest Gump", 1994, "MOVIE"), "Robin Wright", "ACTOR");
        insertMovieCast(movie("Forrest Gump", 1994, "MOVIE"), "Gary Sinise", "ACTOR");
        insertMovieCast(movie("Forrest Gump", 1994, "MOVIE"), "Sally Field", "ACTOR");
        insertMovieCast(movie("Forrest Gump", 1994, "MOVIE"), "Mykelti Williamson", "ACTOR");

        // The Lord of the Rings: The Fellowship of the Ring (15)
        insertMovieCast(movie("The Lord of the Rings: The Fellowship of the Ring", 2001, "MOVIE"), "Peter Jackson", "DIRECTOR");
        insertMovieCast(movie("The Lord of the Rings: The Fellowship of the Ring", 2001, "MOVIE"), "Peter Jackson", "WRITER");
        insertMovieCast(movie("The Lord of the Rings: The Fellowship of the Ring", 2001, "MOVIE"), "Fran Walsh", "WRITER");
        insertMovieCast(movie("The Lord of the Rings: The Fellowship of the Ring", 2001, "MOVIE"), "Philippa Boyens", "WRITER");
        insertMovieCast(movie("The Lord of the Rings: The Fellowship of the Ring", 2001, "MOVIE"), "Elijah Wood", "ACTOR");
        insertMovieCast(movie("The Lord of the Rings: The Fellowship of the Ring", 2001, "MOVIE"), "Ian McKellen", "ACTOR");
        insertMovieCast(movie("The Lord of the Rings: The Fellowship of the Ring", 2001, "MOVIE"), "Viggo Mortensen", "ACTOR");
        insertMovieCast(movie("The Lord of the Rings: The Fellowship of the Ring", 2001, "MOVIE"), "Sean Astin", "ACTOR");
        insertMovieCast(movie("The Lord of the Rings: The Fellowship of the Ring", 2001, "MOVIE"), "Cate Blanchett", "ACTOR");

        // Game of Thrones (16)
        insertMovieCast(movie("Game of Thrones", 2011, "SERIES"), "David Benioff", "DIRECTOR");
        insertMovieCast(movie("Game of Thrones", 2011, "SERIES"), "D.B. Weiss", "DIRECTOR");
        insertMovieCast(movie("Game of Thrones", 2011, "SERIES"), "David Benioff", "WRITER");
        insertMovieCast(movie("Game of Thrones", 2011, "SERIES"), "D.B. Weiss", "WRITER");
        insertMovieCast(movie("Game of Thrones", 2011, "SERIES"), "George R.R. Martin", "WRITER");
        insertMovieCast(movie("Game of Thrones", 2011, "SERIES"), "Emilia Clarke", "ACTOR");
        insertMovieCast(movie("Game of Thrones", 2011, "SERIES"), "Kit Harington", "ACTOR");
        insertMovieCast(movie("Game of Thrones", 2011, "SERIES"), "Lena Headey", "ACTOR");
        insertMovieCast(movie("Game of Thrones", 2011, "SERIES"), "Peter Dinklage", "ACTOR");

        // Stranger Things (17)
        insertMovieCast(movie("Stranger Things", 2016, "SERIES"), "The Duffer Brothers", "DIRECTOR");
        insertMovieCast(movie("Stranger Things", 2016, "SERIES"), "The Duffer Brothers", "WRITER");
        insertMovieCast(movie("Stranger Things", 2016, "SERIES"), "Millie Bobby Brown", "ACTOR");
        insertMovieCast(movie("Stranger Things", 2016, "SERIES"), "Winona Ryder", "ACTOR");
        insertMovieCast(movie("Stranger Things", 2016, "SERIES"), "David Harbour", "ACTOR");
        insertMovieCast(movie("Stranger Things", 2016, "SERIES"), "Finn Wolfhard", "ACTOR");

        // The Godfather (18)
        insertMovieCast(movie("The Godfather", 1972, "MOVIE"), "Francis Ford Coppola", "DIRECTOR");
        insertMovieCast(movie("The Godfather", 1972, "MOVIE"), "Francis Ford Coppola", "WRITER");
        insertMovieCast(movie("The Godfather", 1972, "MOVIE"), "Marlon Brando", "ACTOR");
        insertMovieCast(movie("The Godfather", 1972, "MOVIE"), "Al Pacino", "ACTOR");
        insertMovieCast(movie("The Godfather", 1972, "MOVIE"), "James Caan", "ACTOR");
        insertMovieCast(movie("The Godfather", 1972, "MOVIE"), "Robert Duvall", "ACTOR");

        // The Silence of the Lambs (19)
        insertMovieCast(movie("The Silence of the Lambs", 1991, "MOVIE"), "Jonathan Demme", "DIRECTOR");
        insertMovieCast(movie("The Silence of the Lambs", 1991, "MOVIE"), "Jodie Foster", "ACTOR");
        insertMovieCast(movie("The Silence of the Lambs", 1991, "MOVIE"), "Anthony Hopkins", "ACTOR");
        insertMovieCast(movie("The Silence of the Lambs", 1991, "MOVIE"), "Lawrence A. Bonney", "ACTOR");

        // The Social Network (20)
        insertMovieCast(movie("The Social Network", 2010, "MOVIE"), "David Fincher", "DIRECTOR");
        insertMovieCast(movie("The Social Network", 2010, "MOVIE"), "Aaron Sorkin", "WRITER");
        insertMovieCast(movie("The Social Network", 2010, "MOVIE"), "Jesse Eisenberg", "ACTOR");
        insertMovieCast(movie("The Social Network", 2010, "MOVIE"), "Andrew Garfield", "ACTOR");
        insertMovieCast(movie("The Social Network", 2010, "MOVIE"), "Justin Timberlake", "ACTOR");

        // Parasite (21)
        insertMovieCast(movie("Parasite", 2019, "MOVIE"), "Bong Joon-ho", "DIRECTOR");
        insertMovieCast(movie("Parasite", 2019, "MOVIE"), "Bong Joon-ho", "WRITER");
        insertMovieCast(movie("Parasite", 2019, "MOVIE"), "Song Kang-ho", "ACTOR");
        insertMovieCast(movie("Parasite", 2019, "MOVIE"), "Lee Sun-kyun", "ACTOR");
        insertMovieCast(movie("Parasite", 2019, "MOVIE"), "Cho Yeo-jeong", "ACTOR");
        insertMovieCast(movie("Parasite", 2019, "MOVIE"), "Choi Woo-shik", "ACTOR");
        insertMovieCast(movie("Parasite", 2019, "MOVIE"), "Park So-dam", "ACTOR");

        // The Witcher (22)
        insertMovieCast(movie("The Witcher", 2019, "SERIES"), "Alik Sakharov", "DIRECTOR");
        insertMovieCast(movie("The Witcher", 2019, "SERIES"), "Henry Cavill", "ACTOR");
        insertMovieCast(movie("The Witcher", 2019, "SERIES"), "Anya Chalotra", "ACTOR");
        insertMovieCast(movie("The Witcher", 2019, "SERIES"), "Freya Allan", "ACTOR");

        // Dune (23)
        insertMovieCast(movie("Dune", 2021, "MOVIE"), "Denis Villeneuve", "DIRECTOR");
        insertMovieCast(movie("Dune", 2021, "MOVIE"), "Denis Villeneuve", "WRITER");
        insertMovieCast(movie("Dune", 2021, "MOVIE"), "Timothee Chalamet", "ACTOR");
        insertMovieCast(movie("Dune", 2021, "MOVIE"), "Zendaya", "ACTOR");
        insertMovieCast(movie("Dune", 2021, "MOVIE"), "Rebecca Ferguson", "ACTOR");
        insertMovieCast(movie("Dune", 2021, "MOVIE"), "Oscar Isaac", "ACTOR");
        insertMovieCast(movie("Dune", 2021, "MOVIE"), "Josh Brolin", "ACTOR");
        insertMovieCast(movie("Dune", 2021, "MOVIE"), "Stellan Skarsgard", "ACTOR");

        // Severance (24)
        insertMovieCast(movie("Severance", 2022, "SERIES"), "Ben Stiller", "DIRECTOR");
        insertMovieCast(movie("Severance", 2022, "SERIES"), "Adam Scott", "ACTOR");
        insertMovieCast(movie("Severance", 2022, "SERIES"), "Patricia Arquette", "ACTOR");
        insertMovieCast(movie("Severance", 2022, "SERIES"), "Tramell Tillman", "ACTOR");
        insertMovieCast(movie("Severance", 2022, "SERIES"), "Britt Lower", "ACTOR");
        insertMovieCast(movie("Severance", 2022, "SERIES"), "Zach Cherry", "ACTOR");

        // The Bear (25)
        insertMovieCast(movie("The Bear", 2022, "SERIES"), "Christopher Storer", "DIRECTOR");
        insertMovieCast(movie("The Bear", 2022, "SERIES"), "Christopher Storer", "WRITER");
        insertMovieCast(movie("The Bear", 2022, "SERIES"), "Jeremy Allen White", "ACTOR");
        insertMovieCast(movie("The Bear", 2022, "SERIES"), "Ebon Moss-Bachrach", "ACTOR");
        insertMovieCast(movie("The Bear", 2022, "SERIES"), "Ayo Edebiri", "ACTOR");
        insertMovieCast(movie("The Bear", 2022, "SERIES"), "Liza Colon-Zayas", "ACTOR");

        // Oppenheimer (26)
        insertMovieCast(movie("Oppenheimer", 2023, "MOVIE"), "Christopher Nolan", "DIRECTOR");
        insertMovieCast(movie("Oppenheimer", 2023, "MOVIE"), "Christopher Nolan", "WRITER");
        insertMovieCast(movie("Oppenheimer", 2023, "MOVIE"), "Cillian Murphy", "ACTOR");
        insertMovieCast(movie("Oppenheimer", 2023, "MOVIE"), "Robert Downey Jr.", "ACTOR");
        insertMovieCast(movie("Oppenheimer", 2023, "MOVIE"), "Matt Damon", "ACTOR");
        insertMovieCast(movie("Oppenheimer", 2023, "MOVIE"), "Emily Blunt", "ACTOR");
        insertMovieCast(movie("Oppenheimer", 2023, "MOVIE"), "Florence Pugh", "ACTOR");
        insertMovieCast(movie("Oppenheimer", 2023, "MOVIE"), "Gary Oldman", "ACTOR");

        // The Batman (27)
        insertMovieCast(movie("The Batman", 2022, "MOVIE"), "Matt Reeves", "DIRECTOR");
        insertMovieCast(movie("The Batman", 2022, "MOVIE"), "Matt Reeves", "WRITER");
        insertMovieCast(movie("The Batman", 2022, "MOVIE"), "Robert Pattinson", "ACTOR");
        insertMovieCast(movie("The Batman", 2022, "MOVIE"), "Zoe Kravitz", "ACTOR");
        insertMovieCast(movie("The Batman", 2022, "MOVIE"), "Paul Dano", "ACTOR");
        insertMovieCast(movie("The Batman", 2022, "MOVIE"), "Colin Farrell", "ACTOR");
        insertMovieCast(movie("The Batman", 2022, "MOVIE"), "Jeffrey Wright", "ACTOR");

        // Joker (28)
        insertMovieCast(movie("Joker", 2019, "MOVIE"), "Todd Phillips", "DIRECTOR");
        insertMovieCast(movie("Joker", 2019, "MOVIE"), "Todd Phillips", "WRITER");
        insertMovieCast(movie("Joker", 2019, "MOVIE"), "Joaquin Phoenix", "ACTOR");
        insertMovieCast(movie("Joker", 2019, "MOVIE"), "Robert De Niro", "ACTOR");
        insertMovieCast(movie("Joker", 2019, "MOVIE"), "Zazie Beetz", "ACTOR");
        insertMovieCast(movie("Joker", 2019, "MOVIE"), "Frances Conroy", "ACTOR");

        // Whiplash (29)
        insertMovieCast(movie("Whiplash", 2014, "MOVIE"), "Damien Chazelle", "DIRECTOR");
        insertMovieCast(movie("Whiplash", 2014, "MOVIE"), "Damien Chazelle", "WRITER");
        insertMovieCast(movie("Whiplash", 2014, "MOVIE"), "Miles Teller", "ACTOR");
        insertMovieCast(movie("Whiplash", 2014, "MOVIE"), "J.K. Simmons", "ACTOR");
        insertMovieCast(movie("Whiplash", 2014, "MOVIE"), "Melissa Benoist", "ACTOR");

        // La La Land (30)
        insertMovieCast(movie("La La Land", 2016, "MOVIE"), "Damien Chazelle", "DIRECTOR");
        insertMovieCast(movie("La La Land", 2016, "MOVIE"), "Damien Chazelle", "WRITER");
        insertMovieCast(movie("La La Land", 2016, "MOVIE"), "Ryan Gosling", "ACTOR");
        insertMovieCast(movie("La La Land", 2016, "MOVIE"), "Emma Stone", "ACTOR");
        insertMovieCast(movie("La La Land", 2016, "MOVIE"), "John Legend", "ACTOR");

        // Blade Runner 2049 (31)
        insertMovieCast(movie("Blade Runner 2049", 2017, "MOVIE"), "Denis Villeneuve", "DIRECTOR");
        insertMovieCast(movie("Blade Runner 2049", 2017, "MOVIE"), "Ryan Gosling", "ACTOR");
        insertMovieCast(movie("Blade Runner 2049", 2017, "MOVIE"), "Harrison Ford", "ACTOR");
        insertMovieCast(movie("Blade Runner 2049", 2017, "MOVIE"), "Ana de Armas", "ACTOR");
        insertMovieCast(movie("Blade Runner 2049", 2017, "MOVIE"), "Jared Leto", "ACTOR");

        // Mad Max: Fury Road (32)
        insertMovieCast(movie("Mad Max: Fury Road", 2015, "MOVIE"), "George Miller", "DIRECTOR");
        insertMovieCast(movie("Mad Max: Fury Road", 2015, "MOVIE"), "George Miller", "WRITER");
        insertMovieCast(movie("Mad Max: Fury Road", 2015, "MOVIE"), "Tom Hardy", "ACTOR");
        insertMovieCast(movie("Mad Max: Fury Road", 2015, "MOVIE"), "Charlize Theron", "ACTOR");
        insertMovieCast(movie("Mad Max: Fury Road", 2015, "MOVIE"), "Nicholas Hoult", "ACTOR");

        // The Prestige (33)
        insertMovieCast(movie("The Prestige", 2006, "MOVIE"), "Christopher Nolan", "DIRECTOR");
        insertMovieCast(movie("The Prestige", 2006, "MOVIE"), "Christopher Nolan", "WRITER");
        insertMovieCast(movie("The Prestige", 2006, "MOVIE"), "Jonathan Nolan", "WRITER");
        insertMovieCast(movie("The Prestige", 2006, "MOVIE"), "Christian Bale", "ACTOR");
        insertMovieCast(movie("The Prestige", 2006, "MOVIE"), "Hugh Jackman", "ACTOR");
        insertMovieCast(movie("The Prestige", 2006, "MOVIE"), "Scarlett Johansson", "ACTOR");
        insertMovieCast(movie("The Prestige", 2006, "MOVIE"), "Michael Caine", "ACTOR");
        insertMovieCast(movie("The Prestige", 2006, "MOVIE"), "David Bowie", "ACTOR");

        // Prisoners (34)
        insertMovieCast(movie("Prisoners", 2013, "MOVIE"), "Denis Villeneuve", "DIRECTOR");
        insertMovieCast(movie("Prisoners", 2013, "MOVIE"), "Hugh Jackman", "ACTOR");
        insertMovieCast(movie("Prisoners", 2013, "MOVIE"), "Jake Gyllenhaal", "ACTOR");
        insertMovieCast(movie("Prisoners", 2013, "MOVIE"), "Viola Davis", "ACTOR");
        insertMovieCast(movie("Prisoners", 2013, "MOVIE"), "Melissa Leo", "ACTOR");
        insertMovieCast(movie("Prisoners", 2013, "MOVIE"), "Paul Dano", "ACTOR");

        // Chernobyl (35)
        insertMovieCast(movie("Chernobyl", 2019, "SERIES"), "Johan Renck", "DIRECTOR");
        insertMovieCast(movie("Chernobyl", 2019, "SERIES"), "Jared Harris", "ACTOR");
        insertMovieCast(movie("Chernobyl", 2019, "SERIES"), "Stellan Skarsgard", "ACTOR");

        // Dark (36)
        insertMovieCast(movie("Dark", 2017, "SERIES"), "Baran bo Odar", "DIRECTOR");
        insertMovieCast(movie("Dark", 2017, "SERIES"), "Baran bo Odar", "WRITER");
        insertMovieCast(movie("Dark", 2017, "SERIES"), "Louis Hofmann", "ACTOR");
        insertMovieCast(movie("Dark", 2017, "SERIES"), "Oliver Masucci", "ACTOR");
        insertMovieCast(movie("Dark", 2017, "SERIES"), "Karoline Eichhorn", "ACTOR");

        // Narcos (37)
        insertMovieCast(movie("Narcos", 2015, "SERIES"), "Jose Padilha", "DIRECTOR");
        insertMovieCast(movie("Narcos", 2015, "SERIES"), "Wagner Moura", "ACTOR");
        insertMovieCast(movie("Narcos", 2015, "SERIES"), "Pedro Pascal", "ACTOR");
        insertMovieCast(movie("Narcos", 2015, "SERIES"), "Boyd Holbrook", "ACTOR");

        // Mindhunter (38)
        insertMovieCast(movie("Mindhunter", 2017, "SERIES"), "David Fincher", "DIRECTOR");
        insertMovieCast(movie("Mindhunter", 2017, "SERIES"), "Jonathan Groff", "ACTOR");
        insertMovieCast(movie("Mindhunter", 2017, "SERIES"), "Holt McCallany", "ACTOR");
        insertMovieCast(movie("Mindhunter", 2017, "SERIES"), "Anna Torv", "ACTOR");

        // Black Mirror (39)
        insertMovieCast(movie("Black Mirror", 2011, "SERIES"), "Charlie Brooker", "DIRECTOR");
        insertMovieCast(movie("Black Mirror", 2011, "SERIES"), "Charlie Brooker", "WRITER");
        insertMovieCast(movie("Black Mirror", 2011, "SERIES"), "Jon Hamm", "ACTOR");
        insertMovieCast(movie("Black Mirror", 2011, "SERIES"), "Michael C. Hall", "ACTOR");

        // The Mandalorian (40)
        insertMovieCast(movie("The Mandalorian", 2019, "SERIES"), "Jon Favreau", "DIRECTOR");
        insertMovieCast(movie("The Mandalorian", 2019, "SERIES"), "Jon Favreau", "WRITER");
        insertMovieCast(movie("The Mandalorian", 2019, "SERIES"), "Pedro Pascal", "ACTOR");
        insertMovieCast(movie("The Mandalorian", 2019, "SERIES"), "Carl Weathers", "ACTOR");
        insertMovieCast(movie("The Mandalorian", 2019, "SERIES"), "Timothy Olyphant", "ACTOR");

        // Peaky Blinders (41)
        insertMovieCast(movie("Peaky Blinders", 2013, "SERIES"), "Otto Bathurst", "DIRECTOR");
        insertMovieCast(movie("Peaky Blinders", 2013, "SERIES"), "Cillian Murphy", "ACTOR");
        insertMovieCast(movie("Peaky Blinders", 2013, "SERIES"), "Paul Anderson", "ACTOR");
        insertMovieCast(movie("Peaky Blinders", 2013, "SERIES"), "Sophie Rundle", "ACTOR");
        insertMovieCast(movie("Peaky Blinders", 2013, "SERIES"), "Joe Cole", "ACTOR");

        // Shutter Island (42)
        insertMovieCast(movie("Shutter Island", 2010, "MOVIE"), "Martin Scorsese", "DIRECTOR");
        insertMovieCast(movie("Shutter Island", 2010, "MOVIE"), "Leonardo DiCaprio", "ACTOR");
        insertMovieCast(movie("Shutter Island", 2010, "MOVIE"), "Mark Ruffalo", "ACTOR");
        insertMovieCast(movie("Shutter Island", 2010, "MOVIE"), "Ben Kingsley", "ACTOR");
        insertMovieCast(movie("Shutter Island", 2010, "MOVIE"), "Michelle Williams", "ACTOR");
        insertMovieCast(movie("Shutter Island", 2010, "MOVIE"), "Emily Mortimer", "ACTOR");

        // Arrival (43)
        insertMovieCast(movie("Arrival", 2016, "MOVIE"), "Denis Villeneuve", "DIRECTOR");
        insertMovieCast(movie("Arrival", 2016, "MOVIE"), "Amy Adams", "ACTOR");
        insertMovieCast(movie("Arrival", 2016, "MOVIE"), "Jeremy Renner", "ACTOR");
        insertMovieCast(movie("Arrival", 2016, "MOVIE"), "Forest Whitaker", "ACTOR");
        insertMovieCast(movie("Arrival", 2016, "MOVIE"), "Michael Stuhlbarg", "ACTOR");

        // The Green Mile (44)
        insertMovieCast(movie("The Green Mile", 1999, "MOVIE"), "Frank Darabont", "DIRECTOR");
        insertMovieCast(movie("The Green Mile", 1999, "MOVIE"), "Frank Darabont", "WRITER");
        insertMovieCast(movie("The Green Mile", 1999, "MOVIE"), "Tom Hanks", "ACTOR");
        insertMovieCast(movie("The Green Mile", 1999, "MOVIE"), "Michael Clarke Duncan", "ACTOR");
        insertMovieCast(movie("The Green Mile", 1999, "MOVIE"), "David Morse", "ACTOR");
        insertMovieCast(movie("The Green Mile", 1999, "MOVIE"), "Bonnie Hunt", "ACTOR");

        // The Truman Show (45)
        insertMovieCast(movie("The Truman Show", 1998, "MOVIE"), "Peter Weir", "DIRECTOR");
        insertMovieCast(movie("The Truman Show", 1998, "MOVIE"), "Andrew Niccol", "WRITER");
        insertMovieCast(movie("The Truman Show", 1998, "MOVIE"), "Jim Carrey", "ACTOR");
        insertMovieCast(movie("The Truman Show", 1998, "MOVIE"), "Laura Linney", "ACTOR");
        insertMovieCast(movie("The Truman Show", 1998, "MOVIE"), "Ed Harris", "ACTOR");
        insertMovieCast(movie("The Truman Show", 1998, "MOVIE"), "Noah Emmerich", "ACTOR");
    }

    private void seedRatings() {
        insertRating("user1@example.com", movie("Inception", 2010, "MOVIE"), 4.5);
        insertRating("user1@example.com", movie("Breaking Bad", 2008, "SERIES"), 5.0);
        insertRating("user1@example.com", movie("The Dark Knight", 2008, "MOVIE"), 5.0);
        insertRating("user1@example.com", movie("Interstellar", 2014, "MOVIE"), 4.0);
        insertRating("user1@example.com", movie("The Matrix", 1999, "MOVIE"), 4.5);
        insertRating("user1@example.com", movie("Friends", 1994, "SERIES"), 4.0);
        insertRating("user1@example.com", movie("Gladiator", 2000, "MOVIE"), 4.5);
        insertRating("user1@example.com", movie("Titanic", 1997, "MOVIE"), 3.5);
        insertRating("user1@example.com", movie("The Office", 2005, "SERIES"), 4.5);
        insertRating("user1@example.com", movie("Avengers", 2012, "MOVIE"), 4.0);
        insertRating("user2@example.com", movie("Inception", 2010, "MOVIE"), 5.0);
        insertRating("user2@example.com", movie("Breaking Bad", 2008, "SERIES"), 4.5);
        insertRating("user2@example.com", movie("The Dark Knight", 2008, "MOVIE"), 4.5);
        insertRating("user2@example.com", movie("Interstellar", 2014, "MOVIE"), 5.0);
        insertRating("user2@example.com", movie("The Matrix", 1999, "MOVIE"), 4.0);
        insertRating("user2@example.com", movie("Pulp Fiction", 1994, "MOVIE"), 5.0);
        insertRating("user2@example.com", movie("The Shawshank Redemption", 1994, "MOVIE"), 5.0);
        insertRating("user2@example.com", movie("Fight Club", 1999, "MOVIE"), 4.5);
        insertRating("user2@example.com", movie("Forrest Gump", 1994, "MOVIE"), 4.5);
        insertRating("user2@example.com", movie("The Lord of the Rings: The Fellowship of the Ring", 2001, "MOVIE"), 5.0);
        insertRating("user3@example.com", movie("Inception", 2010, "MOVIE"), 4.0);
        insertRating("user3@example.com", movie("The Dark Knight", 2008, "MOVIE"), 5.0);
        insertRating("user3@example.com", movie("Interstellar", 2014, "MOVIE"), 4.5);
        insertRating("user3@example.com", movie("Game of Thrones", 2011, "SERIES"), 4.5);
        insertRating("user3@example.com", movie("Stranger Things", 2016, "SERIES"), 4.0);
        insertRating("user3@example.com", movie("The Godfather", 1972, "MOVIE"), 5.0);
        insertRating("user3@example.com", movie("The Silence of the Lambs", 1991, "MOVIE"), 4.5);
        insertRating("user3@example.com", movie("The Social Network", 2010, "MOVIE"), 4.0);
        insertRating("user3@example.com", movie("Parasite", 2019, "MOVIE"), 5.0);
        insertRating("user3@example.com", movie("Dune", 2021, "MOVIE"), 4.5);
        insertRating("user4@example.com", movie("Inception", 2010, "MOVIE"), 4.5);
        insertRating("user4@example.com", movie("Breaking Bad", 2008, "SERIES"), 5.0);
        insertRating("user4@example.com", movie("The Dark Knight", 2008, "MOVIE"), 4.0);
        insertRating("user4@example.com", movie("Oppenheimer", 2023, "MOVIE"), 5.0);
        insertRating("user4@example.com", movie("The Batman", 2022, "MOVIE"), 4.0);
        insertRating("user4@example.com", movie("Joker", 2019, "MOVIE"), 4.5);
        insertRating("user4@example.com", movie("Whiplash", 2014, "MOVIE"), 5.0);
        insertRating("user4@example.com", movie("La La Land", 2016, "MOVIE"), 4.0);
        insertRating("user4@example.com", movie("Blade Runner 2049", 2017, "MOVIE"), 4.5);
        insertRating("user4@example.com", movie("Mad Max: Fury Road", 2015, "MOVIE"), 4.5);
        insertRating("user5@example.com", movie("The Prestige", 2006, "MOVIE"), 5.0);
        insertRating("user5@example.com", movie("Prisoners", 2013, "MOVIE"), 4.5);
        insertRating("user5@example.com", movie("Chernobyl", 2019, "SERIES"), 5.0);
        insertRating("user5@example.com", movie("Dark", 2017, "SERIES"), 4.5);
        insertRating("user5@example.com", movie("Narcos", 2015, "SERIES"), 4.0);
        insertRating("user5@example.com", movie("Mindhunter", 2017, "SERIES"), 4.5);
        insertRating("user5@example.com", movie("Black Mirror", 2011, "SERIES"), 4.0);
        insertRating("user5@example.com", movie("The Mandalorian", 2019, "SERIES"), 4.0);
        insertRating("user5@example.com", movie("Peaky Blinders", 2013, "SERIES"), 4.5);
        insertRating("user5@example.com", movie("Shutter Island", 2010, "MOVIE"), 4.5);
        insertRating("user6@example.com", movie("Arrival", 2016, "MOVIE"), 4.5);
        insertRating("user6@example.com", movie("The Green Mile", 1999, "MOVIE"), 5.0);
        insertRating("user6@example.com", movie("The Truman Show", 1998, "MOVIE"), 4.5);
        insertRating("user6@example.com", movie("Pulp Fiction", 1994, "MOVIE"), 4.5);
        insertRating("user6@example.com", movie("The Shawshank Redemption", 1994, "MOVIE"), 5.0);
        insertRating("user6@example.com", movie("Fight Club", 1999, "MOVIE"), 4.0);
        insertRating("user6@example.com", movie("Forrest Gump", 1994, "MOVIE"), 5.0);
        insertRating("user6@example.com", movie("The Lord of the Rings: The Fellowship of the Ring", 2001, "MOVIE"), 4.5);
        insertRating("user6@example.com", movie("Game of Thrones", 2011, "SERIES"), 4.0);
        insertRating("user6@example.com", movie("Stranger Things", 2016, "SERIES"), 4.5);
        insertRating("user7@example.com", movie("The Godfather", 1972, "MOVIE"), 5.0);
        insertRating("user7@example.com", movie("The Silence of the Lambs", 1991, "MOVIE"), 4.0);
        insertRating("user7@example.com", movie("The Social Network", 2010, "MOVIE"), 4.5);
        insertRating("user7@example.com", movie("Parasite", 2019, "MOVIE"), 4.5);
        insertRating("user7@example.com", movie("Dune", 2021, "MOVIE"), 4.0);
        insertRating("user7@example.com", movie("Oppenheimer", 2023, "MOVIE"), 4.5);
        insertRating("user7@example.com", movie("The Batman", 2022, "MOVIE"), 4.5);
        insertRating("user7@example.com", movie("Joker", 2019, "MOVIE"), 4.0);
        insertRating("user7@example.com", movie("Whiplash", 2014, "MOVIE"), 4.5);
        insertRating("user7@example.com", movie("La La Land", 2016, "MOVIE"), 4.5);
        insertRating("user8@example.com", movie("Blade Runner 2049", 2017, "MOVIE"), 4.0);
        insertRating("user8@example.com", movie("Mad Max: Fury Road", 2015, "MOVIE"), 5.0);
        insertRating("user8@example.com", movie("The Prestige", 2006, "MOVIE"), 4.5);
        insertRating("user8@example.com", movie("Prisoners", 2013, "MOVIE"), 4.0);
        insertRating("user8@example.com", movie("Chernobyl", 2019, "SERIES"), 4.5);
        insertRating("user8@example.com", movie("Dark", 2017, "SERIES"), 4.0);
        insertRating("user8@example.com", movie("Narcos", 2015, "SERIES"), 4.5);
        insertRating("user8@example.com", movie("Mindhunter", 2017, "SERIES"), 4.0);
        insertRating("user8@example.com", movie("Black Mirror", 2011, "SERIES"), 4.5);
        insertRating("user8@example.com", movie("The Mandalorian", 2019, "SERIES"), 4.5);
        insertRating("user9@example.com", movie("Peaky Blinders", 2013, "SERIES"), 5.0);
        insertRating("user9@example.com", movie("Shutter Island", 2010, "MOVIE"), 4.0);
        insertRating("user9@example.com", movie("Arrival", 2016, "MOVIE"), 5.0);
        insertRating("user9@example.com", movie("The Green Mile", 1999, "MOVIE"), 4.5);
        insertRating("user9@example.com", movie("The Truman Show", 1998, "MOVIE"), 4.0);
        insertRating("user9@example.com", movie("Inception", 2010, "MOVIE"), 5.0);
        insertRating("user9@example.com", movie("Breaking Bad", 2008, "SERIES"), 4.5);
        insertRating("user9@example.com", movie("The Dark Knight", 2008, "MOVIE"), 5.0);
        insertRating("user9@example.com", movie("Interstellar", 2014, "MOVIE"), 4.5);
        insertRating("user9@example.com", movie("The Matrix", 1999, "MOVIE"), 4.0);
    }

    private void seedReviews() {
        insertReview("user1@example.com", movie("Inception", 2010, "MOVIE"), "Mind-bending masterpiece! Nolan at his best.");
        insertReview("user1@example.com", movie("Breaking Bad", 2008, "SERIES"), "The greatest TV show ever made.");
        insertReview("user1@example.com", movie("The Dark Knight", 2008, "MOVIE"), "Heath Ledger's performance is legendary.");
        insertReview("user2@example.com", movie("Interstellar", 2014, "MOVIE"), "An emotional journey through space and time.");
        insertReview("user2@example.com", movie("The Matrix", 1999, "MOVIE"), "Revolutionary film that changed cinema.");
        insertReview("user2@example.com", movie("Pulp Fiction", 1994, "MOVIE"), "Tarantino's dialogue is unmatched.");
        insertReview("user3@example.com", movie("The Shawshank Redemption", 1994, "MOVIE"), "Hope is a good thing. The best movie ever.");
        insertReview("user3@example.com", movie("Fight Club", 1999, "MOVIE"), "The first rule of Fight Club is... just watch it.");
        insertReview("user3@example.com", movie("Forrest Gump", 1994, "MOVIE"), "Life is like a box of chocolates. Beautiful film.");
        insertReview("user4@example.com", movie("The Godfather", 1972, "MOVIE"), "An offer you can't refuse. Absolute classic.");
        insertReview("user4@example.com", movie("Parasite", 2019, "MOVIE"), "Brilliant social commentary with perfect pacing.");
        insertReview("user4@example.com", movie("Oppenheimer", 2023, "MOVIE"), "A stunning achievement in filmmaking.");
        insertReview("user5@example.com", movie("Chernobyl", 2019, "SERIES"), "Terrifying and educational. Must-watch.");
        insertReview("user5@example.com", movie("Dark", 2017, "SERIES"), "The most complex time-travel story ever told.");
        insertReview("user5@example.com", movie("The Prestige", 2006, "MOVIE"), "Are you watching closely? Incredible twist.");
        insertReview("user6@example.com", movie("The Green Mile", 1999, "MOVIE"), "Tom Hanks and Michael Clarke Duncan are phenomenal.");
        insertReview("user6@example.com", movie("The Truman Show", 1998, "MOVIE"), "Prophetic and deeply moving. Jim Carrey is brilliant.");
        insertReview("user6@example.com", movie("Arrival", 2016, "MOVIE"), "Intelligent sci-fi that makes you think.");
        insertReview("user7@example.com", movie("Whiplash", 2014, "MOVIE"), "J.K. Simmons gives one of the best performances ever.");
        insertReview("user7@example.com", movie("La La Land", 2016, "MOVIE"), "A beautiful love letter to old Hollywood.");
        insertReview("user8@example.com", movie("Mad Max: Fury Road", 2015, "MOVIE"), "Pure adrenaline from start to finish.");
        insertReview("user8@example.com", movie("Blade Runner 2049", 2017, "MOVIE"), "A visual masterpiece that improves on the original.");
        insertReview("user9@example.com", movie("Peaky Blinders", 2013, "SERIES"), "Cillian Murphy is mesmerizing as Thomas Shelby.");
        insertReview("user9@example.com", movie("Shutter Island", 2010, "MOVIE"), "Scorsese's best thriller. The ending stays with you.");
    }

    private void seedUserLists() {
        // Create WATCHLIST, WATCHED and FAVORITES for each user
        // Uses ensureUserListByType which checks by type (not name) to avoid duplicates
        for (long userId = 1; userId <= 9; userId++) {
            ensureUserListByType(userId, "Do obejrzenia", "WATCHLIST");
            ensureUserListByType(userId, "Obejrzane", "WATCHED");
            ensureUserListByType(userId, "Ulubione", "FAVORITES");
        }
        // Admin user (id = 10)
        ensureUserListByType(10L, "Do obejrzenia", "WATCHLIST");
        ensureUserListByType(10L, "Obejrzane", "WATCHED");
        ensureUserListByType(10L, "Ulubione", "FAVORITES");
    }

    private void seedUserListItems() {
        // Add movies to WATCHLIST for each user
        addUserListItemByUserAndType(1L, "WATCHLIST", 1L);  // Inception
        addUserListItemByUserAndType(1L, "WATCHLIST", 3L);  // The Dark Knight
        addUserListItemByUserAndType(1L, "WATCHLIST", 10L); // Avengers
        addUserListItemByUserAndType(2L, "WATCHLIST", 4L);  // Interstellar
        addUserListItemByUserAndType(2L, "WATCHLIST", 5L);  // The Matrix
        addUserListItemByUserAndType(3L, "WATCHLIST", 7L);  // Gladiator
        addUserListItemByUserAndType(3L, "WATCHLIST", 8L);  // Titanic
        addUserListItemByUserAndType(4L, "WATCHLIST", 11L); // Pulp Fiction
        addUserListItemByUserAndType(4L, "WATCHLIST", 12L); // The Shawshank Redemption
        addUserListItemByUserAndType(5L, "WATCHLIST", 15L); // The Lord of the Rings
        addUserListItemByUserAndType(5L, "WATCHLIST", 18L); // The Godfather
        addUserListItemByUserAndType(6L, "WATCHLIST", 21L); // Parasite
        addUserListItemByUserAndType(6L, "WATCHLIST", 23L); // Dune
        addUserListItemByUserAndType(7L, "WATCHLIST", 26L); // Oppenheimer
        addUserListItemByUserAndType(7L, "WATCHLIST", 28L); // Joker
        addUserListItemByUserAndType(8L, "WATCHLIST", 32L); // Mad Max: Fury Road
        addUserListItemByUserAndType(8L, "WATCHLIST", 33L); // The Prestige
        addUserListItemByUserAndType(9L, "WATCHLIST", 42L); // Shutter Island
        addUserListItemByUserAndType(9L, "WATCHLIST", 44L); // The Green Mile

        // Add movies to WATCHED list for each user (so profile stats show real counts)
        addUserListItemByUserAndType(1L, "WATCHED", 2L);  // Breaking Bad
        addUserListItemByUserAndType(1L, "WATCHED", 4L);  // Interstellar
        addUserListItemByUserAndType(1L, "WATCHED", 5L);  // The Matrix
        addUserListItemByUserAndType(1L, "WATCHED", 6L);  // Friends
        addUserListItemByUserAndType(2L, "WATCHED", 1L);  // Inception
        addUserListItemByUserAndType(2L, "WATCHED", 3L);  // The Dark Knight
        addUserListItemByUserAndType(2L, "WATCHED", 7L);  // Gladiator
        addUserListItemByUserAndType(2L, "WATCHED", 11L); // Pulp Fiction
        addUserListItemByUserAndType(3L, "WATCHED", 8L);  // Titanic
        addUserListItemByUserAndType(3L, "WATCHED", 12L); // The Shawshank Redemption
        addUserListItemByUserAndType(3L, "WATCHED", 13L); // Fight Club
        addUserListItemByUserAndType(3L, "WATCHED", 14L); // Forrest Gump
        addUserListItemByUserAndType(4L, "WATCHED", 18L); // The Godfather
        addUserListItemByUserAndType(4L, "WATCHED", 21L); // Parasite
        addUserListItemByUserAndType(4L, "WATCHED", 26L); // Oppenheimer
        addUserListItemByUserAndType(4L, "WATCHED", 27L); // The Batman
        addUserListItemByUserAndType(5L, "WATCHED", 32L); // Mad Max: Fury Road
        addUserListItemByUserAndType(5L, "WATCHED", 33L); // The Prestige
        addUserListItemByUserAndType(5L, "WATCHED", 35L); // Chernobyl
        addUserListItemByUserAndType(5L, "WATCHED", 36L); // Dark
        addUserListItemByUserAndType(6L, "WATCHED", 42L); // Shutter Island
        addUserListItemByUserAndType(6L, "WATCHED", 44L); // The Green Mile
        addUserListItemByUserAndType(6L, "WATCHED", 45L); // The Truman Show
        addUserListItemByUserAndType(7L, "WATCHED", 28L); // Joker
        addUserListItemByUserAndType(7L, "WATCHED", 29L); // Whiplash
        addUserListItemByUserAndType(7L, "WATCHED", 30L); // La La Land
        addUserListItemByUserAndType(8L, "WATCHED", 31L); // Blade Runner 2049
        addUserListItemByUserAndType(8L, "WATCHED", 34L); // Prisoners
        addUserListItemByUserAndType(9L, "WATCHED", 41L); // Peaky Blinders
        addUserListItemByUserAndType(9L, "WATCHED", 43L); // Arrival
    }

    private void seedReports() {
        // No default reports needed
    }

    // Helper methods

    private void insertMovieCast(SeedMovieRef movie, String personName, String roleName) {
        Long movieId = jdbcTemplate.query(
            "SELECT id FROM movies WHERE title = ? AND release_year = ? AND type = ?",
            (rs) -> rs.next() ? rs.getLong("id") : null,
            movie.title(), movie.releaseYear(), movie.type());
        Long personId = jdbcTemplate.query(
            "SELECT id FROM people WHERE name = ?",
            (rs) -> rs.next() ? rs.getLong("id") : null,
            personName);
        Long roleId = jdbcTemplate.query(
            "SELECT id FROM roles WHERE name = ?",
            (rs) -> rs.next() ? rs.getLong("id") : null,
            roleName);
        if (movieId != null && personId != null && roleId != null) {
            jdbcTemplate.update(
                "INSERT INTO movie_cast (movie_id, person_id, role_id) VALUES (?, ?, ?) ON CONFLICT DO NOTHING",
                movieId, personId, roleId);
        }
    }

    private void insertRating(String email, SeedMovieRef movie, double rating) {
        Long userId = jdbcTemplate.query("SELECT id FROM users WHERE email = ?",
            (rs) -> rs.next() ? rs.getLong("id") : null, email);
        Long movieId = jdbcTemplate.query(
            "SELECT id FROM movies WHERE title = ? AND release_year = ? AND type = ?",
            (rs) -> rs.next() ? rs.getLong("id") : null,
            movie.title(), movie.releaseYear(), movie.type());
        if (userId != null && movieId != null) {
            jdbcTemplate.update(
                "INSERT INTO ratings (user_id, movie_id, rating) VALUES (?, ?, ?) ON CONFLICT DO NOTHING",
                userId, movieId, rating);
        }
    }

    private void insertReview(String email, SeedMovieRef movie, String content) {
        Long userId = jdbcTemplate.query("SELECT id FROM users WHERE email = ?",
            (rs) -> rs.next() ? rs.getLong("id") : null, email);
        Long movieId = jdbcTemplate.query(
            "SELECT id FROM movies WHERE title = ? AND release_year = ? AND type = ?",
            (rs) -> rs.next() ? rs.getLong("id") : null,
            movie.title(), movie.releaseYear(), movie.type());
        if (userId != null && movieId != null) {
            jdbcTemplate.update(
                "INSERT INTO reviews (user_id, movie_id, content) VALUES (?, ?, ?)",
                userId, movieId, content);
        }
    }

    private void createUserListIfMissing(Long userId, String name, String type) {
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM user_lists WHERE user_id = ? AND name = ? AND type = ?",
            Integer.class, userId, name, type);
        if (count != null && count == 0) {
            jdbcTemplate.update(
                "INSERT INTO user_lists (user_id, name, type) VALUES (?, ?, ?)",
                userId, name, type);
        }
    }

    private void ensureUserListByType(Long userId, String name, String type) {
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM user_lists WHERE user_id = ? AND type = ?",
            Integer.class, userId, type);
        if (count != null && count == 0) {
            jdbcTemplate.update(
                "INSERT INTO user_lists (user_id, name, type) VALUES (?, ?, ?)",
                userId, name, type);
        }
    }

    private void addUserListItem(Long listId, Long movieId) {
        jdbcTemplate.update(
            "INSERT INTO user_list_items (list_id, movie_id, position) VALUES (?, ?, ?) ON CONFLICT DO NOTHING",
            listId, movieId, 0);
    }

    private void addUserListItemByUserAndType(Long userId, String listType, Long movieId) {
        Long listId = jdbcTemplate.query(
            "SELECT id FROM user_lists WHERE user_id = ? AND type = ?",
            (rs) -> rs.next() ? rs.getLong("id") : null,
            userId, listType);
        if (listId != null) {
            jdbcTemplate.update(
                "INSERT INTO user_list_items (list_id, movie_id, position) VALUES (?, ?, ?) ON CONFLICT DO NOTHING",
                listId, movieId, 0);
        }
    }

    private void insertIfMissing(String sql, String param1, String param2) {
        jdbcTemplate.update(sql, param1, param2);
    }

    private void upsertUser(String username, String email, String passwordHash, String role) {
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM users WHERE email = ?", Integer.class, email);
        if (count != null && count == 0) {
            jdbcTemplate.update(
                "INSERT INTO users (username, email, password_hash, role) VALUES (?, ?, ?, ?)",
                username, email, passwordHash, role);
        }
    }

    private void insertMovie(String title, String description, int releaseYear, String type) {
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM movies WHERE title = ? AND release_year = ? AND type = ?",
            Integer.class, title, releaseYear, type);
        if (count != null && count == 0) {
            jdbcTemplate.update(
                "INSERT INTO movies (title, description, release_year, type) VALUES (?, ?, ?, ?)",
                title, description, releaseYear, type);
        }
    }

    private void insertMovieGenre(SeedMovieRef movie, String genreName) {
        Long movieId = jdbcTemplate.query(
            "SELECT id FROM movies WHERE title = ? AND release_year = ? AND type = ?",
            (rs) -> rs.next() ? rs.getLong("id") : null,
            movie.title(), movie.releaseYear(), movie.type());
        Long genreId = jdbcTemplate.query(
            "SELECT id FROM genres WHERE name = ?",
            (rs) -> rs.next() ? rs.getLong("id") : null, genreName);
        if (movieId != null && genreId != null) {
            jdbcTemplate.update(
                "INSERT INTO movie_genres (movie_id, genre_id) VALUES (?, ?) ON CONFLICT DO NOTHING",
                movieId, genreId);
        }
    }

    private SeedMovieRef movie(String title, int releaseYear, String type) {
        return new SeedMovieRef(title, releaseYear, type);
    }
}
