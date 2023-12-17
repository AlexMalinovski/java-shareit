DELETE FROM comments;
DELETE FROM bookings;
DELETE FROM items;
DELETE FROM requests;
DELETE FROM users;

INSERT INTO users (id, name, email, email_lowercase)
VALUES (1, 'owner', 'owner@mail.ru', 'owner@mail.ru'), (2, 'user', 'user@mail.ru', 'user@mail.ru');
