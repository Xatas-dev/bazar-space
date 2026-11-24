--liquibase formatted sql
--changeset g1ggl3r:1
--description create join_request table

create table join_request (
    id bigserial primary key,
    space_id bigint not null references space (id) on delete cascade,
    user_id uuid not null,
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone not null default now(),
    constraint uq_join_request_space_id_user_id unique (space_id, user_id)
);

create index join_request_space_id_created_at_idx on join_request (space_id, created_at);

comment on column join_request.id is 'Id of join request';
comment on column join_request.space_id is 'Space the user requests to join';
comment on column join_request.user_id is 'User who requested to join the space';
comment on column join_request.created_at is 'Created at timestamp';
comment on column join_request.updated_at is 'Updated at timestamp';
