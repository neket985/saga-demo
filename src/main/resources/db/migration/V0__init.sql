create type COMPLETION_TYPE as enum ('IN_PROGRESS', 'COMPLETE', 'ERROR');
create type STEP_COMPLETION_TYPE as enum ('WAIT','SUCCESS', 'ERROR', 'ROLLBACK');
create type TRANSACTION_TYPE as enum ('COMPENSATABLE', 'RETRIABLE');

create table saga
(
    id                 serial primary key,
    orchestrator_alias varchar         not null,
    completion_state   COMPLETION_TYPE not null,
    tries_count        int             not null default 0,
    next_tries_at      timestamp       not null default now(),
    inserted_at        timestamp       not null default now(),
    updated_at         timestamp       not null default now()
);

create table saga_step
(
    id               serial primary key,
    saga_id          int references saga  not null,
    step_number      int                  not null,
    completion_state STEP_COMPLETION_TYPE not null,
    tries_count      int                  not null default 0,
    transaction_type TRANSACTION_TYPE     not null,
    context          bytea,
    inserted_at      timestamp            not null default now(),
    updated_at       timestamp            not null default now()

);
create unique index saga_id_and_step_number_uindex on saga_step (saga_id, step_number);

create table saga_step_error
(
    id            serial primary key,
    saga_id       int references saga not null,
    saga_step_id  int references saga_step,
    tries_counter int                 not null,
    description   varchar             not null,
    inserted_at   timestamp           not null default now()
);
