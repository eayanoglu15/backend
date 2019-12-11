CREATE TABLE trip(
                      ID SERIAL PRIMARY KEY NOT NULL,
                      version timestamp without time zone,
                      driver_name varchar(255),
                      from_where varchar(255),
                      to_where varchar(255),
                      start_time timestamp ,
                      end_time timestamp ,
                      total_seat_number INT,
                      available_seat_number INT
);
alter table public.trip
    add constraint trip_driver_name
        foreign key (driver_name)
            references public.users (username);

CREATE TABLE trip_request(
                     ID SERIAL PRIMARY KEY NOT NULL,
                     version timestamp without time zone,
                     trip_id BIGINT,
                     showable boolean,
                     person_requested varchar(255),
                     is_accepted boolean
);
alter table public.trip_request
    add constraint trip_request_trip_id
        foreign key (trip_id)
            references public.trip (id),
    add constraint trip_request_username
        foreign key (person_requested)
            references public.users (username);

CREATE TABLE trip_vote(
                             ID SERIAL PRIMARY KEY NOT NULL,
                             version timestamp without time zone,
                             trip_request_id BIGINT,
                             vote_given_by_driver INT,
                             vote_given_by_hitchhiker INT
);
alter table public.trip_vote
    add constraint trip_vote_trip_request_id
        foreign key (trip_request_id)
            references public.trip_request (id);