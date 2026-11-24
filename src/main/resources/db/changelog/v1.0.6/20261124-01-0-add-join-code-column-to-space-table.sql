--liquibase formatted sql
--changeset g1ggl3r:2
--description add join_code column to space table and fill it for existing spaces

create extension if not exists pgcrypto;

alter table space
add column join_code varchar(32);

-- 16 random bytes encoded as base64url without padding (22 chars), same format as the application generator
update space
set join_code = rtrim(translate(encode(gen_random_bytes(16), 'base64'), '+/', '-_'), '=')
where join_code is null;

alter table space
alter column join_code set not null;

alter table space
add constraint uq_space_join_code unique (join_code);

comment on column space.join_code is 'Permanent unique invite code of the space';
