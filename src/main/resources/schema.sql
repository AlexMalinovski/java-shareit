--DROP INDEX IF EXISTS email_unique_idx;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(512) NOT NULL,
  email_lowercase VARCHAR(512) NOT NULL,
  CONSTRAINT pk_user PRIMARY KEY (id),
  CONSTRAINT UQ_USER_EMAIL_LOWERCASE UNIQUE (email_lowercase)
);

--CREATE UNIQUE INDEX email_unique_idx on users (LOWER(email));

CREATE TABLE IF NOT EXISTS items (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(512) NOT NULL,
  available BOOLEAN NOT NULL,
  owner_id BIGINT NOT NULL,
  CONSTRAINT pk_item PRIMARY KEY (id),
  CONSTRAINT fk_owner_id FOREIGN KEY (owner_id)
      REFERENCES users (id)
      ON UPDATE NO ACTION
      ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS bookings (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  status VARCHAR(20) NOT NULL,
  start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  end_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  item_id BIGINT NOT NULL,
  booker_id BIGINT NOT NULL,
  CONSTRAINT pk_booking PRIMARY KEY (id),
  CONSTRAINT fk_booking_item_id FOREIGN KEY (item_id)
      REFERENCES items (id)
      ON UPDATE NO ACTION
      ON DELETE CASCADE,
  CONSTRAINT fk_booker_id FOREIGN KEY (booker_id)
        REFERENCES users (id)
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS comments (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  text VARCHAR(512) NOT NULL,
  created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  item_id BIGINT NOT NULL,
  author_id BIGINT NOT NULL,
  CONSTRAINT pk_comment PRIMARY KEY (id),
  CONSTRAINT fk_comment_item_id FOREIGN KEY (item_id)
      REFERENCES items (id)
      ON UPDATE NO ACTION
      ON DELETE CASCADE,
  CONSTRAINT fk_author_id FOREIGN KEY (author_id)
        REFERENCES users (id)
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);
