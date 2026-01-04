--liquibase formatted sql
--changeset AsterYng:2
--description Create table user_space
create table user_space(
    id bigserial primary key,
    space_id bigint references space(id) not null,
    user_id uuid not null,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now()
);

alter table user_space
add constraint space_user_ids_unique unique (space_id, user_id);

comment on column user_space.id is 'Id of exact user in the exact space';
comment on column user_space.space_id is 'Space id foreign key';
comment on column user_space.user_id is 'User id';
comment on column user_space.created_at is 'Created at timestamp';
comment on column user_space.updated_at is 'Updated at timestamp';
