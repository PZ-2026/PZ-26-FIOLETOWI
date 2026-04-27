CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('USER', 'ADMIN')),
    is_blocked BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS movies (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    release_year INT,
    type VARCHAR(20) CHECK (type IN ('MOVIE', 'SERIES')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_movies_title_release_type UNIQUE (title, release_year, type)
);

CREATE TABLE IF NOT EXISTS genres (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS movie_genres (
    movie_id INT NOT NULL,
    genre_id INT NOT NULL,
    PRIMARY KEY (movie_id, genre_id)
);

CREATE TABLE IF NOT EXISTS ratings (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    movie_id INT NOT NULL,
    rating INT CHECK (rating BETWEEN 1 AND 10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (user_id, movie_id)
);

CREATE TABLE IF NOT EXISTS reviews (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    movie_id INT NOT NULL,
    content TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS user_lists (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    name VARCHAR(255),
    type VARCHAR(20) CHECK (type IN ('WATCHLIST', 'WATCHED', 'FAVORITES', 'CUSTOM')),
    CONSTRAINT uq_user_lists_user_name UNIQUE (user_id, name)
);

CREATE TABLE IF NOT EXISTS user_list_items (
    id SERIAL PRIMARY KEY,
    list_id INT NOT NULL,
    movie_id INT NOT NULL,
    position INT,
    UNIQUE (list_id, movie_id)
);

CREATE TABLE IF NOT EXISTS reports (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    type VARCHAR(100),
    generated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    file_path TEXT
);

CREATE TABLE IF NOT EXISTS roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS people (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS movie_cast (
    movie_id INT NOT NULL,
    person_id INT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (movie_id, person_id, role_id)
);

DO $$ 
BEGIN 
  BEGIN
    ALTER TABLE movies ADD CONSTRAINT uq_movies_title_release_type UNIQUE (title, release_year, type);
  EXCEPTION WHEN others THEN END;
  BEGIN
    ALTER TABLE user_lists ADD CONSTRAINT uq_user_lists_user_name UNIQUE (user_id, name);
  EXCEPTION WHEN others THEN END;
  BEGIN
    ALTER TABLE movie_genres ADD CONSTRAINT fk_movie_genres_movie FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE;
  EXCEPTION WHEN others THEN END;
  BEGIN
    ALTER TABLE movie_genres ADD CONSTRAINT fk_movie_genres_genre FOREIGN KEY (genre_id) REFERENCES genres(id) ON DELETE CASCADE;
  EXCEPTION WHEN others THEN END;
  BEGIN
    ALTER TABLE ratings ADD CONSTRAINT fk_ratings_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;
  EXCEPTION WHEN others THEN END;
  BEGIN
    ALTER TABLE ratings ADD CONSTRAINT fk_ratings_movie FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE;
  EXCEPTION WHEN others THEN END;
  BEGIN
    ALTER TABLE reviews ADD CONSTRAINT fk_reviews_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;
  EXCEPTION WHEN others THEN END;
  BEGIN
    ALTER TABLE reviews ADD CONSTRAINT fk_reviews_movie FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE;
  EXCEPTION WHEN others THEN END;
  BEGIN
    ALTER TABLE user_lists ADD CONSTRAINT fk_user_lists_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;
  EXCEPTION WHEN others THEN END;
  BEGIN
    ALTER TABLE user_list_items ADD CONSTRAINT fk_user_list_items_list FOREIGN KEY (list_id) REFERENCES user_lists(id) ON DELETE CASCADE;
  EXCEPTION WHEN others THEN END;
  BEGIN
    ALTER TABLE user_list_items ADD CONSTRAINT fk_user_list_items_movie FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE;
  EXCEPTION WHEN others THEN END;
  BEGIN
    ALTER TABLE reports ADD CONSTRAINT fk_reports_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;
  EXCEPTION WHEN others THEN END;
  BEGIN
    ALTER TABLE movie_cast ADD CONSTRAINT fk_movie_cast_movie FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE;
  EXCEPTION WHEN others THEN END;
  BEGIN
    ALTER TABLE movie_cast ADD CONSTRAINT fk_movie_cast_person FOREIGN KEY (person_id) REFERENCES people(id) ON DELETE CASCADE;
  EXCEPTION WHEN others THEN END;
  BEGIN
    ALTER TABLE movie_cast ADD CONSTRAINT fk_movie_cast_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE;
  EXCEPTION WHEN others THEN END;
END $$;
