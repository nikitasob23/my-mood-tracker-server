CREATE TABLE users(
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    nickname VARCHAR(255),
    password VARCHAR(255) NOT NULL
);

CREATE TABLE mood_entries(
    id SERIAL PRIMARY KEY,
    degree INT NOT NULL,
    date_time TIMESTAMP NOT NULL ,
    user_id BIGINT REFERENCES users(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE mood_tags (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    degree INT,
    mood_entry_id BIGINT REFERENCES mood_entries(id),
    FOREIGN KEY (mood_entry_id) REFERENCES mood_entries(id)
);