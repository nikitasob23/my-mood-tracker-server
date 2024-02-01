CREATE TABLE test_mood_tracker.usrs
(
    id       BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255)
);

CREATE TABLE test_mood_tracker.mood_entries
(
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    degree    INT,
    date_time TIMESTAMP,
    user_id   BIGINT,
    CONSTRAINT fk_mood_entries_user_id FOREIGN KEY (user_id) REFERENCES usrs (id) ON DELETE CASCADE
);

CREATE TABLE test_mood_tracker.mood_tags
(
    id      BIGINT PRIMARY KEY AUTO_INCREMENT,
    name    VARCHAR(15) UNIQUE NOT NULL,
    degree  INT,
    user_id BIGINT,
    CONSTRAINT fk_mood_tags_user_id FOREIGN KEY (user_id) REFERENCES usrs (id) ON DELETE CASCADE
);

CREATE TABLE test_mood_tracker.mood_entry_tag
(
    entry_id BIGINT,
    tag_id   BIGINT,
    CONSTRAINT fk_mood_entry_tag_entry_id FOREIGN KEY (entry_id) REFERENCES mood_entries (id) ON DELETE CASCADE,
    CONSTRAINT fk_mood_entry_tag_tag_id FOREIGN KEY (tag_id) REFERENCES mood_tags (id) ON DELETE CASCADE
);