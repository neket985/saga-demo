create type COMPLETION_TYPE as enum ('IN_PROGRESS', 'COMPLETE', 'ERROR');
create type STEP_COMPLETION_TYPE as enum ('WAIT','SUCCESS', 'ERROR', 'ROLLBACK');
create type TRANSACTION_TYPE as enum ('COMPENSATABLE', 'RETRIABLE');

create table saga
(
    id                 serial primary key,
    orchestrator_alias varchar,
    completion_state   COMPLETION_TYPE,
    tries_count        int       default 0,
    next_tries_at      timestamp default now(),
    inserted_at        timestamp default now(),
    updated_at         timestamp default now()
);

create table saga_step
(
    id               serial primary key,
    saga_id          int references saga,
    step_number      int,
    completion_state STEP_COMPLETION_TYPE,
    tries_count      int       default 0,
    transaction_type TRANSACTION_TYPE,
    context          bytea null,
    inserted_at      timestamp default now(),
    updated_at       timestamp default now()

);
create unique index saga_id_and_step_number_uindex on saga_step (saga_id, step_number);

create table saga_step_error
(
    id            serial primary key,
    saga_id       int references saga,
    saga_step_id  int references saga_step,
    tries_counter int,
    description   varchar,
    inserted_at   timestamp default now()
);
