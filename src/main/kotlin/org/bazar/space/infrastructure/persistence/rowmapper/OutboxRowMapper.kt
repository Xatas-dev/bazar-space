package org.bazar.space.infrastructure.persistence.rowmapper

import org.bazar.space.infrastructure.persistence.entity.Outbox
import org.bazar.space.infrastructure.persistence.entity.OutboxStatus
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class OutboxRowMapper :
    RowMapper<Outbox> {

    override fun mapRow(rs: ResultSet, rowNum: Int): Outbox =
        Outbox(
            id = rs.getLong("id"),
            entity = rs.getString("entity"),
            entityId = rs.getLong("entity_id"),
            payload = rs.getString("payload"),
            status = OutboxStatus.valueOf(rs.getString("status")),
            createdAt = rs.getTimestamp("created_at").toInstant(),
            updatedAt = rs.getTimestamp("updated_at").toInstant()
        )

}