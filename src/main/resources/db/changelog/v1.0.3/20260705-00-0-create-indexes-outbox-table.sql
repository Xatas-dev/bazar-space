--liquibase formatted sql
--changeset AsterYng:1
--description create indexes on outbox table

create index entity_status_idx on outbox (entity, status)
    where status in ('NEW', 'ERROR');

create index updated_at_idx on outbox (updated_at)
    where status = 'IN_PROGRESS';