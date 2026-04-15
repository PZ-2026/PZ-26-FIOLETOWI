-- Ignoruj duplikaty (jeśli wykonano więcej niż 1 raz)
UPDATE users SET password_hash = '$2a$10$WVHMrwX2wpE0SveYBbOlbu5dM.HyYFR4OWCRELYtyk8AFEixIUiUe';

INSERT INTO users (username, email, password_hash, role) VALUES
('user1', 'user1@example.com', '$2a$10$WVHMrwX2wpE0SveYBbOlbu5dM.HyYFR4OWCRELYtyk8AFEixIUiUe', 'USER'),
('user2', 'user2@example.com', '$2a$10$WVHMrwX2wpE0SveYBbOlbu5dM.HyYFR4OWCRELYtyk8AFEixIUiUe', 'USER'),
('user3', 'user3@example.com', '$2a$10$WVHMrwX2wpE0SveYBbOlbu5dM.HyYFR4OWCRELYtyk8AFEixIUiUe', 'USER'),
('user4', 'user4@example.com', '$2a$10$WVHMrwX2wpE0SveYBbOlbu5dM.HyYFR4OWCRELYtyk8AFEixIUiUe', 'USER'),
('user5', 'user5@example.com', '$2a$10$WVHMrwX2wpE0SveYBbOlbu5dM.HyYFR4OWCRELYtyk8AFEixIUiUe', 'USER'),
('user6', 'user6@example.com', '$2a$10$WVHMrwX2wpE0SveYBbOlbu5dM.HyYFR4OWCRELYtyk8AFEixIUiUe', 'USER'),
('user7', 'user7@example.com', '$2a$10$WVHMrwX2wpE0SveYBbOlbu5dM.HyYFR4OWCRELYtyk8AFEixIUiUe', 'USER'),
('user8', 'user8@example.com', '$2a$10$WVHMrwX2wpE0SveYBbOlbu5dM.HyYFR4OWCRELYtyk8AFEixIUiUe', 'USER'),
('user9', 'user9@example.com', '$2a$10$WVHMrwX2wpE0SveYBbOlbu5dM.HyYFR4OWCRELYtyk8AFEixIUiUe', 'USER'),
('admin', 'admin@example.com', '$2a$10$WVHMrwX2wpE0SveYBbOlbu5dM.HyYFR4OWCRELYtyk8AFEixIUiUe', 'ADMIN');

INSERT INTO genres (name) VALUES
('Action'), ('Drama'), ('Comedy'), ('Sci-Fi'), ('Horror'),
('Thriller'), ('Romance'), ('Fantasy'), ('Adventure'), ('Crime');

INSERT INTO movies (title, description, release_year, type) VALUES
('Inception', 'Dreams within dreams', 2010, 'MOVIE'),
('Breaking Bad', 'Drug empire story', 2008, 'SERIES'),
('The Dark Knight', 'Batman vs Joker', 2008, 'MOVIE'),
('Interstellar', 'Space exploration', 2014, 'MOVIE'),
('The Matrix', 'Virtual reality', 1999, 'MOVIE'),
('Friends', 'Group of friends', 1994, 'SERIES'),
('Gladiator', 'Roman general revenge', 2000, 'MOVIE'),
('Titanic', 'Ship tragedy love story', 1997, 'MOVIE'),
('The Office', 'Mockumentary office life', 2005, 'SERIES'),
('Avengers', 'Superhero team', 2012, 'MOVIE');

INSERT INTO movie_genres VALUES
(1,4),(1,1),(2,2),(3,1),(3,2),
(4,4),(5,4),(6,3),(7,1),(8,7),
(9,3),(10,1),(10,9),(7,2),(5,1);

INSERT INTO roles (name) VALUES
('ACTOR'), ('DIRECTOR'), ('WRITER'), ('PRODUCER'), ('CINEMATOGRAPHER');

INSERT INTO people (name) VALUES
('Leonardo DiCaprio'),
('Christopher Nolan'),
('Keanu Reeves'),
('Bryan Cranston'),
('Aaron Paul'),
('Matthew McConaughey'),
('Russell Crowe'),
('Kate Winslet'),
('Steve Carell'),
('Robert Downey Jr.');

INSERT INTO movie_cast VALUES
(1,1,1),(1,2,2),(3,2,2),
(5,3,1),(2,4,1),(2,5,1),
(4,6,1),(7,7,1),(8,8,1),
(9,9,1),(10,10,1),
(4,2,2),(10,2,3),(7,2,3),(5,2,2);

INSERT INTO ratings (user_id, movie_id, rating) VALUES
(1,1,9),(2,1,8),(3,2,9),(4,3,10),(5,4,9),
(6,5,8),(7,6,7),(8,7,9),(9,8,8),(10,9,7);

INSERT INTO reviews (user_id, movie_id, content) VALUES
(1,1,'Great movie'),
(2,1,'Mind blowing'),
(3,2,'Amazing series'),
(4,3,'Best Batman'),
(5,4,'Epic sci-fi'),
(6,5,'Classic'),
(7,6,'Very funny'),
(8,7,'Powerful story'),
(9,8,'Emotional'),
(10,9,'Hilarious');

INSERT INTO user_lists (user_id, name, type) VALUES
(1,'Watchlist','WATCHLIST'),
(2,'Favorites','FAVORITES'),
(3,'Watched','WATCHED'),
(4,'My List','CUSTOM'),
(5,'Sci-Fi','CUSTOM'),
(6,'Top','CUSTOM'),
(7,'Later','WATCHLIST'),
(8,'Best','FAVORITES'),
(9,'Seen','WATCHED'),
(10,'Mix','CUSTOM');

INSERT INTO user_list_items (list_id, movie_id, position) VALUES
(1,1,1),(1,2,2),(2,3,1),(2,4,2),(3,5,1),
(4,6,1),(5,1,1),(6,7,1),(7,8,1),(8,9,1),
(9,10,1),(10,2,1),(3,4,2),(5,7,2),(6,8,2);

INSERT INTO reports (user_id, type, file_path) VALUES
(1,'ACTIVITY','/r1.pdf'),
(2,'ACTIVITY','/r2.pdf'),
(3,'ACTIVITY','/r3.pdf'),
(4,'ACTIVITY','/r4.pdf'),
(5,'ACTIVITY','/r5.pdf'),
(6,'ACTIVITY','/r6.pdf'),
(7,'ACTIVITY','/r7.pdf'),
(8,'ACTIVITY','/r8.pdf'),
(9,'ACTIVITY','/r9.pdf'),
(10,'ADMIN','/r10.pdf');
