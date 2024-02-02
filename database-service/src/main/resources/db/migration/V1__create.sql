CREATE TABLE usrs
(
    id       BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) UNIQUE NOT NULL,
    nickname VARCHAR(255),
    password VARCHAR(255) NOT NULL
);

CREATE TABLE mood_entries
(
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    degree    INT,
    date_time TIMESTAMP,
    user_id   BIGINT NOT NULL,
    CONSTRAINT fk_mood_entries_user_id FOREIGN KEY (user_id) REFERENCES usrs (id) ON DELETE CASCADE
);

CREATE TABLE mood_tags
(
    id      BIGINT PRIMARY KEY AUTO_INCREMENT,
    name    VARCHAR(15) NOT NULL,
    degree  INT,
    user_id BIGINT NOT NULL,
    UNIQUE mood_tags_unique_name_user_id (name, user_id),
    CONSTRAINT fk_mood_tags_user_id FOREIGN KEY (user_id) REFERENCES usrs (id) ON DELETE CASCADE
);

CREATE TABLE mood_entry_tag
(
    entry_id BIGINT,
    tag_id   BIGINT,
    CONSTRAINT fk_mood_entry_tag_entry_id FOREIGN KEY (entry_id) REFERENCES mood_entries (id) ON DELETE CASCADE,
    CONSTRAINT fk_mood_entry_tag_tag_id FOREIGN KEY (tag_id) REFERENCES mood_tags (id) ON DELETE CASCADE
);
