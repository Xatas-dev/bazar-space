package org.bazar.space.persistence.repository

import org.bazar.space.persistence.entity.Outbox
import org.bazar.space.persistence.rowmapper.OutboxRowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Repository
import java.sql.Timestamp
import java.time.Duration
import java.time.Instant

@Repository
class OutboxRepository(
    private val jdbcOperations: NamedParameterJdbcOperations,
    private val outboxRowMapper: OutboxRowMapper
) {

    companion object {

        private val FIND_AND_LOCK_NEXT_BATCH_WHERE_ENTITY = """
            with to_lock as materialized (
                select id from outbox
                where status = 'NEW'
                and entity = :entity
                limit :limit for update skip locked
            ) update outbox o
            set
               updated_at = now(),
               status = 'IN_PROGRESS'
            from to_lock tl
            where o.id = tl.id
            returning *
        """.trimIndent()

        private val INSERT_ALL = """
            insert into outbox (entity, entity_id, payload, status, updated_at, created_at)
            values (:entity, :entity_id, :payload::jsonb, :status, :updated_at, :created_at)
        """.trimIndent()

        private val UPDATE_STATUS_BATCH = """
            update outbox set
                status = :status,
                updated_at = :updated_at
            where id = :id
        """.trimIndent()

        private val RESET_LOCKS = """
            with to_reset as (
                select id from outbox
                where status = 'IN_PROGRESS' and updated_at < :timestampThreshold
                limit :limit
            )
            update outbox o set
            status = 'NEW',
            updated_at = :updated_at
            from to_reset tr
            where o.id = tr.id
        """.trimIndent()
    }

    fun updateStatusBatch(entities: Collection<Outbox>) {
        val batchParams = entities.map { outbox ->
            MapSqlParameterSource()
                .addValue("id", outbox.id)
                .addValue("status", outbox.status.name)
                .addValue("updated_at", Timestamp.from(outbox.updatedAt))
        }.toTypedArray()

        jdbcOperations.batchUpdate(UPDATE_STATUS_BATCH, batchParams)
    }

    fun insertAll(entities: Collection<Outbox>) {
        val batchParams = entities.map { outbox ->
            MapSqlParameterSource()
                .addValue("entity", outbox.entity.name)
                .addValue("entity_id", outbox.entityId)
                .addValue("payload", outbox.payload)
                .addValue("status", outbox.status.name)
                .addValue("updated_at", Timestamp.from(outbox.updatedAt))
                .addValue("created_at", Timestamp.from(outbox.createdAt))
        }.toTypedArray()

        jdbcOperations.batchUpdate(INSERT_ALL, batchParams)
    }

    fun findAndLockNextBatchWhereEntity(entity: String, limit: Int): List<Outbox> {
        val params = MapSqlParameterSource()
            .addValue("entity", entity)
            .addValue("limit", limit)

        return jdbcOperations.query(FIND_AND_LOCK_NEXT_BATCH_WHERE_ENTITY, params, outboxRowMapper)
    }

    fun resetLock(threshold: Duration, limit: Int): Int {
        val timestampThreshold = Instant.now().minus(threshold)
        val params = MapSqlParameterSource()
            .addValue("timestampThreshold", Timestamp.from(timestampThreshold))
            .addValue("updated_at", Timestamp.from(Instant.now()))
            .addValue("limit", limit)

        return jdbcOperations.update(RESET_LOCKS, params)
    }
}