DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS status;
DROP TABLE IF EXISTS request;
DROP TABLE IF EXISTS users;


CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(512) NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
    );

CREATE TABLE IF NOT EXISTS request(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    description VARCHAR(100),
    requester_id int4 NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE,
    FOREIGN KEY (requester_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS status (
    id   int4 GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS items (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    available BOOLEAN,
    owner_id int4 NOT NULL,
    request_id int4 NOT NULL,
    FOREIGN KEY (owner_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (request_id) REFERENCES request (id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS bookings(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    start_date TIMESTAMP WITHOUT TIME ZONE,
    end_date TIMESTAMP WITHOUT TIME ZONE,
    item_id int4 NOT NULL,
    booker_id int4 NOT NULL,
    status_id int4 NOT NULL,
    FOREIGN KEY (item_id) REFERENCES items (id) ON DELETE CASCADE,
    FOREIGN KEY (booker_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (status_id) REFERENCES status (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS comments(
     id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
     text VARCHAR(1000) NOT NULL,
     item_id int4 NOT NULL,
     author_id int4 NOT NULL,
    FOREIGN KEY (item_id) REFERENCES items (id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE CASCADE
);

INSERT INTO status (name)
VALUES ('WAITING'),
       ('APPROVED'),
       ('REJECTED'),
       ('CANCELED');
