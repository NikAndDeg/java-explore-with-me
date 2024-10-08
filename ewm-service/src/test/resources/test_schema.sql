DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS locations CASCADE;
DROP TABLE IF EXISTS event_states CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS compilations CASCADE;
DROP TABLE IF EXISTS compilations_events CASCADE;
DROP TABLE IF EXISTS participation_statuses CASCADE;
DROP TABLE IF EXISTS comments CASCADE;

CREATE TABLE IF NOT EXISTS users (
    user_id int GENERATED BY DEFAULT AS IDENTITY,
    user_name varchar(250) NOT NULL,
    user_email varchar(254) NOT NULL,

    CONSTRAINT pk_users PRIMARY KEY(user_id),
    CONSTRAINT unique_user_email UNIQUE(user_email)
);
INSERT INTO users (user_name, user_email)
VALUES ('mr. User', 'mruser@mail.com'),
       ('mr. Second User', 'mrseconduser@mail.com');


CREATE TABLE IF NOT EXISTS categories (
    category_id int GENERATED BY DEFAULT AS IDENTITY,
    category_name varchar(50) NOT NULL,

    CONSTRAINT pk_catigories PRIMARY KEY(category_id),
    CONSTRAINT unique_category_name UNIQUE(category_name)
);

INSERT INTO categories (category_name)
VALUES ('category 1'),
       ('category 2');

CREATE TABLE IF NOT EXISTS locations (
    location_id int GENERATED BY DEFAULT AS IDENTITY,
    lat real NOT NULL,
    lon real NOT NULL,

    CONSTRAINT pk_location PRIMARY KEY(location_id)
);

INSERT INTO locations (lat, lon)
VALUES (66, 55);

CREATE TABLE IF NOT EXISTS event_states (
    event_state_id int GENERATED BY DEFAULT AS IDENTITY,
    event_state_name varchar(50) NOT NULL,

    CONSTRAINT pk_event_states PRIMARY KEY(event_state_id),
    CONSTRAINT unique_event_state_name UNIQUE(event_state_name)
);

INSERT INTO event_states (event_state_name)
VALUES ('PENDING'),
       ('PUBLISHED'),
       ('CANCELED');

CREATE TABLE IF NOT EXISTS events (
    event_id int GENERATED BY DEFAULT AS IDENTITY,
    category_id int NOT NULL,
    initiator_id int NOT NULL,
    location_id int NOT NULL,
    event_state_id int NOT NULL,
    title varchar(120) NOT NULL,
    annotation varchar(2000) NOT NULL,
    description varchar(7000) NOT NULL,
    participant_limit int NOT NULL,
    confirmed_requests int NOT NULL,
    created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    published_on TIMESTAMP WITHOUT TIME ZONE,
    paid boolean NOT NULL,
    request_moderation boolean NOT NULL,

    CONSTRAINT pk_events PRIMARY KEY(event_id),
    CONSTRAINT fk_events_categories FOREIGN KEY(category_id)
        REFERENCES categories(category_id),
    CONSTRAINT fk_events_users FOREIGN KEY(initiator_id)
        REFERENCES users(user_id),
    CONSTRAINT fk_events_locations FOREIGN KEY(location_id)
        REFERENCES locations(location_id),
    CONSTRAINT fk_events_event_states FOREIGN KEY(event_state_id)
        REFERENCES event_states(event_state_id)
);

INSERT INTO events (category_id, initiator_id, location_id, event_state_id, title, annotation, description,
participant_limit, confirmed_requests, created_on, event_date, published_on, paid, request_moderation)
VALUES (1, 1, 1, 2, 'title 1', 'annotation 1', 'description 1', 10, 9, '2020-01-01 12:00:00', '2025-01-01 12:00:00',
'2021-01-01 10:00:00', 1, 0),
       (1, 1, 1, 2, 'title 2', 'annotation 2', 'description 2', 10, 1, '2020-01-01 12:00:00', '1999-01-01 12:00:00',
'2021-01-01 10:00:00', 1, 0),
       (1, 1, 1, 2, 'title 3', 'annotation 3', 'description 3', 10, 1, '2020-01-01 12:00:00', '1999-01-01 12:00:00',
'2021-01-01 10:00:00', 1, 0),
       (2, 1, 1, 2, 'title 4', 'annotation 4', 'description 4', 0, 100, '2020-01-01 12:00:00', '2025-01-01 12:00:00',
'2021-01-01 10:00:00', 0, 0),
       (2, 1, 1, 3, 'title 5', 'annotation 5', 'description 5', 0, 100, '2020-01-01 12:00:00', '2025-01-01 12:00:00',
'2021-01-01 10:00:00', 1, 0);

CREATE TABLE IF NOT EXISTS compilations (
    compilation_id int GENERATED BY DEFAULT AS IDENTITY,
    compilation_title varchar(50) NOT NULL,
    pinned boolean NOT NULL,

    CONSTRAINT pk_compilations PRIMARY KEY(compilation_id),
    CONSTRAINT unique_compilation_title UNIQUE(compilation_title)
);

INSERT INTO compilations (compilation_title, pinned)
VALUES ('compilation_title', 1);

CREATE TABLE IF NOT EXISTS compilations_events (
    compilation_event_id int GENERATED BY DEFAULT AS IDENTITY,
    compilation_id int NOT NULL,
    event_id int NOT NULL,

    CONSTRAINT pk_compilations_events PRIMARY KEY(compilation_event_id),
    CONSTRAINT fk_compilations_events_compilations FOREIGN KEY(compilation_id)
        REFERENCES compilations(compilation_id),
    CONSTRAINT fk_compilations_events_events FOREIGN KEY(event_id)
        REFERENCES events(event_id)
);

INSERT INTO compilations_events (compilation_id, event_id)
VALUES (1, 2),
       (1, 1);

CREATE TABLE IF NOT EXISTS participation_statuses (
    status_id int GENERATED BY DEFAULT AS IDENTITY,
    status_name varchar(50) NOT NULL,

    CONSTRAINT pk_participation_statuses PRIMARY KEY(status_id),
    CONSTRAINT unique_status_name UNIQUE(status_name)
);

INSERT INTO participation_statuses (status_name)
VALUES ('PENDING'),
       ('CONFIRMED'),
       ('REJECTED'),
       ('CANCELED');

CREATE TABLE IF NOT EXISTS participation_requests (
    request_id int GENERATED BY DEFAULT AS IDENTITY,
    user_id int NOT NULL,
    event_id int NOT NULL,
    status_id int NOT NULL,
    created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,

    CONSTRAINT pk_participation_requests PRIMARY KEY(request_id),
    CONSTRAINT fk_participation_requests_users FOREIGN KEY(user_id)
        REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_participation_requests_events FOREIGN KEY(event_id)
        REFERENCES events(event_id),
    CONSTRAINT fk_participation_requests_participation_statuses FOREIGN KEY(status_id)
        REFERENCES participation_statuses(status_id)
);

INSERT INTO participation_requests (user_id, event_id, status_id, created_on)
VALUES (2, 1, 2, '2020-01-01 12:00:00');

CREATE TABLE IF NOT EXISTS comments (
    comment_id int GENERATED BY DEFAULT AS IDENTITY,
    event_id int NOT NULL,
    user_id int NOT NULL,
    text varchar(1000) NOT NULL,
    created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,

    CONSTRAINT pk_comments PRIMARY KEY(comment_id),
    CONSTRAINT fk_comments_users FOREIGN KEY(user_id)
        REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_comments_events FOREIGN KEY(event_id)
        REFERENCES events(event_id) ON DELETE CASCADE
);

INSERT INTO comments (event_id, user_id, text, created_on)
VALUES (1, 1, 'text 1 qwerty', '1999-01-01 12:00:00'),
       (1, 1, 'text 2 qweeeeeee', '2021-01-01 12:00:00'),
       (1, 2, 'text 3', '2100-01-01 12:00:00'),
       (2, 1, 'text 4', '2020-01-01 12:00:00'),
       (2, 1, 'text 4', '2021-01-01 12:00:00');