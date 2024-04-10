INSERT INTO usrs(id, username, nickname, encoded_password) VALUES (1, 'clark@gmail.com', 'clark', 'PASSWORD_1');
INSERT INTO usrs(id, username, nickname, encoded_password) VALUES (2, 'dave@gmail.com', 'dave', 'PASSWORD_2');
INSERT INTO usrs(id, username, nickname, encoded_password) VALUES (3, 'ava@yandex.ru', 'ava', 'PASSWORD_3');

INSERT INTO mood_entries(id, date_time, degree, user_id) VALUES (1, '2024-04-09 21:10:24', 4, 1);
INSERT INTO mood_entries(id, date_time, degree, user_id) VALUES (2, '2024-04-09 10:11:56', 3, 1);
INSERT INTO mood_entries(id, date_time, degree, user_id) VALUES (3, '2024-04-10 11:04:07', 3, 2);

INSERT INTO mood_tags(id, name, degree, user_id) VALUES (1, 'friends', 4, 1);
INSERT INTO mood_tags(id, name, degree, user_id) VALUES (2, 'work', 3, 1);
INSERT INTO mood_tags(id, name, degree, user_id) VALUES (3, 'family', 3, 1);
INSERT INTO mood_tags(id, name, degree, user_id) VALUES (4, 'family', 4, 2);
INSERT INTO mood_tags(id, name, degree, user_id) VALUES (5, 'books', 4, 2);

INSERT INTO mood_entry_tag(entry_id, tag_id) VALUES (1, 1);
INSERT INTO mood_entry_tag(entry_id, tag_id) VALUES (1, 2);
INSERT INTO mood_entry_tag(entry_id, tag_id) VALUES (1, 3);
INSERT INTO mood_entry_tag(entry_id, tag_id) VALUES (2, 1);
INSERT INTO mood_entry_tag(entry_id, tag_id) VALUES (2, 3);
INSERT INTO mood_entry_tag(entry_id, tag_id) VALUES (3, 4);
INSERT INTO mood_entry_tag(entry_id, tag_id) VALUES (3, 5);