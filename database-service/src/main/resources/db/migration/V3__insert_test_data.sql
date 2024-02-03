INSERT INTO usrs VALUES (1, 'clark@gmail.com', 'clark', 'PASSWORD_1');
INSERT INTO usrs VALUES (2, 'dave@gmail.com', 'dave', 'PASSWORD_2');
INSERT INTO usrs VALUES (3, 'ava@yandex.ru', 'ava', 'PASSWORD_3');

INSERT INTO mood_entries VALUES (1, 4, '2024-01-18 21:10:24.346878', 1);
INSERT INTO mood_entries VALUES (2, 3, '2024-01-20 10:11:56.447351', 1);
INSERT INTO mood_entries VALUES (3, 3, '2024-02-01 14:56:23.016792', 2);

INSERT INTO mood_tags VALUES (1, 'friends', 4, 1);
INSERT INTO mood_tags VALUES (2, 'work', 3, 1);
INSERT INTO mood_tags VALUES (3, 'family', 3, 1);
INSERT INTO mood_tags VALUES (4, 'family', 4, 2);
INSERT INTO mood_tags VALUES (5, 'books', 4, 2);

INSERT INTO mood_entry_tag VALUES (1, 1);
INSERT INTO mood_entry_tag VALUES (1, 2);
INSERT INTO mood_entry_tag VALUES (1, 3);
INSERT INTO mood_entry_tag VALUES (2, 1);
INSERT INTO mood_entry_tag VALUES (2, 3);
INSERT INTO mood_entry_tag VALUES (3, 4);
INSERT INTO mood_entry_tag VALUES (3, 5);