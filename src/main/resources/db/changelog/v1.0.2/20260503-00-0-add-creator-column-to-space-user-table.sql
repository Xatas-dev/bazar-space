--liquibase formatted sql
--changeset AsterYng:1
--description add creator column to space_user table
alter table user_space
add column creator bool not null default false;

alter table user_space
alter column creator drop default;

comment on column user_space.creator IS 'Defining creator of space';