CREATE TABLE IF NOT EXISTS films(
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name varchar(255) NOT NULL ,
    description varchar(200),
    release_date date NOT NULL,
    duration integer NOT NULL,
    mpa_id integer NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    email varchar(255) NOT NULL,
    login varchar(255) NOT NULL,
    name varchar(255) NOT NULL ,
    birthday date NOT NULL
);

CREATE TABLE IF NOT EXISTS genres (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name varchar(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS mpa (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name varchar(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS film_genre (
    film_id integer,
    genre_id integer,
    FOREIGN KEY (film_id) REFERENCES films(id),
    FOREIGN KEY (genre_id) REFERENCES genres(id)
);

CREATE TABLE IF NOT EXISTS likes (
    film_id integer,
    user_id integer,
    FOREIGN KEY (film_id) REFERENCES films(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS follows (
    following_user_id integer,
    followed_user_id integer,
    FOREIGN KEY (following_user_id) REFERENCES users(id),
    FOREIGN KEY (followed_user_id) REFERENCES users(id)
);

ALTER TABLE films
ADD FOREIGN KEY (mpa_id) REFERENCES mpa(id);