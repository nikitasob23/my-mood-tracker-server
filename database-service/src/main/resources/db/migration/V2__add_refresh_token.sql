CREATE TABLE encoded_auth_tokens
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    access VARCHAR(255) NOT NULL,
    refresh VARCHAR(255) NOT NULL,
    CONSTRAINT fk_refresh_token_user_id FOREIGN KEY (user_id) REFERENCES usrs (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX uk_auth_tokens_user_id ON usrs(id);

ALTER TABLE usrs RENAME COLUMN password TO encoded_password;

CREATE UNIQUE INDEX uk_usrs_username ON usrs(username);

CREATE UNIQUE INDEX uk_mood_tags_name_user_id ON mood_tags(name, user_id);
