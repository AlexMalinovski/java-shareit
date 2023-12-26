DELETE FROM comments;
DELETE FROM bookings;
DELETE FROM items;
DELETE FROM requests;
DELETE FROM users;

INSERT INTO users (id, name, email, email_lowercase)
VALUES (1, 'owner', 'owner@mail.ru', 'owner@mail.ru'), (2, 'user', 'user@mail.ru', 'user@mail.ru');

INSERT INTO requests (id, description, created, user_id)
VALUES (1, 'request1', '2022-01-01T00:00:00', 2),
       (2, 'request2', '2022-01-02T00:00:00', 1),
       (3, 'request3', '2022-01-03T00:00:00', 2);

INSERT INTO items (id, name, description, available, owner_id, request_id)
VALUES (1, 'item1', 'item1', true, 1, null), (2, 'item2', 'item2', false, 1, null), (3, 'item3', 'item3', true, 1, 1);