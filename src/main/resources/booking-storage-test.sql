INSERT INTO users (id, name, email, email_lowercase)
VALUES (1, 'owner', 'owner@mail.ru', 'owner@mail.ru'), (2, 'user', 'user@mail.ru', 'user@mail.ru');

INSERT INTO items (id, name, description, available, owner_id)
VALUES (1, 'item1', 'item1', true, 1), (2, 'item2', 'item2', false, 1), (3, 'item3', 'item3', true, 1);

INSERT INTO comments (id, text, created, item_id, author_id)
VALUES (1, 'comment','2022-01-01T00:00:00' , 1, 2);

INSERT INTO bookings (id, status, start_date, end_date, item_id, booker_id)
VALUES (1, 'APPROVED', '2022-01-01T00:00:00', '2022-01-02T00:00:00', 1, 2),
       (2, 'WAITING', '2022-01-03T00:00:00', '2022-01-04T00:00:00', 1, 2),
       (3, 'APPROVED', '2022-01-05T00:00:00', '2022-01-06T00:00:00', 1, 2);
