ALTER TABLE usrs DROP COLUMN nickname;
ALTER TABLE usrs ADD COLUMN email VARCHAR(25);

ALTER TABLE usrs ADD UNIQUE KEY uk_usrs_email (email);
