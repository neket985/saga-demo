create type ORDER_STATE as enum ('CREATED_PENDING', 'CREATED', 'REJECTED');

create table ship_order
(
    id          serial primary key,
    state       ORDER_STATE not null,
    inserted_at timestamp   not null default now(),
    updated_at  timestamp   not null default now()
);