CREATE TABLE users(
    ID SERIAL PRIMARY KEY NOT NULL,
    email       VARCHAR(255),
    password VARCHAR(255),
    first_name       VARCHAR(255),
    surname VARCHAR(255),
    username VARCHAR(255),
    phone VARCHAR(255),
    age INT,
    sex VARCHAR(255),
    driver BOOLEAN DEFAULT FALSE,
    plaque varchar(255),
    car_model varchar(255),
    point double precision,
    number_revieved int,
    version timestamp without time zone
);
alter table public.users
    add constraint users_unique_key
        unique (username);
