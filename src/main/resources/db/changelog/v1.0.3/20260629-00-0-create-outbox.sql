--liquibase formatted sql
--changeset AsterYng:1
--description create outbox table

create table outbox (
    id bigserial primary key,
    entity varchar(32) not null,
    entity_id bigint not null,
    payload jsonb not null,
    status varchar(32) not null,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null
);

comment on column outbox.entity is 'Entity name (SPACE, ...)';
comment on column outbox.status is 'Outbox status (NEW, DONE, ERROR)';
comment on column outbox.entity_id is 'Entity ID';
comment on column outbox.payload is 'Event payload';