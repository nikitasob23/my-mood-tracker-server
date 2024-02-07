INSERT INTO usrs(id, username, nickname, password) VALUES (1, 'clark@gmail.com', 'clark', 'PASSWORD_1');
INSERT INTO usrs(id, username, nickname, password) VALUES (2, 'dave@gmail.com', 'dave', 'PASSWORD_2');
INSERT INTO usrs(id, username, nickname, password) VALUES (3, 'ava@yandex.ru', 'ava', 'PASSWORD_3');

INSERT INTO mood_entries(id, date_time, degree, user_id) VALUES (1, '2024-01-18 21:10:24.346878', 4, 1);
INSERT INTO mood_entries(id, date_time, degree, user_id) VALUES (2, '2024-01-20 10:11:56.447351', 3, 1);
INSERT INTO mood_entries(id, date_time, degree, user_id) VALUES (3, '2024-02-01 14:56:23.016792', 3, 2);

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