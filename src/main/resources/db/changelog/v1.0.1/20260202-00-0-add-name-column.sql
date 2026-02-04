--liquibase formatted sql
--changeset AsterYng:1
--description add name column
alter table space
add column name varchar(128) not null default 'поменяй меня пж';


comment on column space.name IS 'Space name';