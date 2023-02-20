-- This is for performing tests
-- ROLE_ADMIN.id = 1
-- ROLE_USER.id = 2
INSERT INTO role (authority)
VALUES ('ROLE_ADMIN'),
       ('ROLE_USER');

-- admin.password == "password"
-- user.password == "password"
INSERT INTO user (username, password, email)
VALUES ('admin', '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW', 'admin@mail.ru'),
       ('user', '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW', 'user@mail.ru');

-- admin.role == ROLE_ADMIN
-- user.role == ROLE_USER
INSERT INTO user_role (user_id, role_id)
VALUES (1, 1), -- These id's values will be only if database was created initially
       (1, 2),
       (2, 2);


-- add other users for test
-- passwords still the same ("password")
INSERT INTO user (first_name, last_name, username, password, email)
VALUES ('John', 'Doe', 'user1', '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW', 'user1@mail.ru'),
       ('Brandon', 'Hunt', 'user2', '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW', 'user2@mail.ru'),
       ('Cassandra', 'Fox', 'user3', '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW',
        'user3@mail.ru'),
       ('Charlene', 'Austin', 'user4', '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW', 'user4@mail.ru'),
       ('Terrence', 'Hicks', 'user5', '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW', 'user5@mail.ru'),
       ('Alexa', 'Lawson', 'user6', '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW', 'user6@mail.ru'),
       ('Aaron', 'Lee', 'user7', '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW', 'user7@mail.ru'),
       ('Aubrey', 'Turner', 'user8', '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW', 'user8@mail.ru');


-- give roles to these users
INSERT INTO user_role (user_id, role_id)
VALUES (3, 1),
       (3, 2),
       (4, 2),
       (5, 2),
       (6, 2),
       (7, 2),
       (8, 1),
       (8, 2),
       (9, 2),
       (10, 2);
