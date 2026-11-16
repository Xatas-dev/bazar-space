--liquibase formatted sql
--changeset g1ggl3r:1
--description create space_avatar table

create table space_avatar (
    id bigserial primary key,
    space_id bigint not null references space (id),
    file_id uuid not null,
    is_active boolean not null,
    status varchar(16) not null,
    url text,
    error_reason text,
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone not null default now(),
    constraint uq_space_avatar_file_id unique (file_id)
);

comment on column space_avatar.space_id is 'Space that owns the avatar';
comment on column space_avatar.file_id is 'File uuid from bazar-files';
comment on column space_avatar.is_active is 'Whether this avatar is the currently displayed one';
comment on column space_avatar.status is 'Avatar upload status (PENDING, SUCCESS, ERROR)';
comment on column space_avatar.url is 'Public download url after successful upload';
comment on column space_avatar.error_reason is 'Reason when upload failed';