create type ORDER_FISCAL_STATE as enum ('FISCALIZED_PENDING', 'FISCALIZED', 'REJECTED');

create table order_fiscal
(
    id          serial primary key,
    order_id    int                not null,
    state       ORDER_FISCAL_STATE not null,
    inserted_at timestamp          not null default now(),
    updated_at  timestamp          not null default now()
);