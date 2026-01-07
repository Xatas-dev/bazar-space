--liquibase formatted sql
--changeset AsterYng:1
--description Create table space
create table space(
    id bigserial primary key,
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone not null default now()
);

comment on column space.id IS 'Id space';
comment on column space.created_at IS 'Space created timestamp';
comment on column space.updated_at IS 'Space updated timestamp';