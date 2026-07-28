package org.bazar.space.persistence.rowmapper

import org.bazar.space.model.events.EntityType
import org.bazar.space.persistence.entity.Outbox
import org.bazar.space.persistence.entity.enums.OutboxStatus
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class OutboxRowMapper :
    RowMapper<Outbox> {

    override fun mapRow(rs: ResultSet, rowNum: Int): Outbox =
        Outbox(
            id = rs.getLong("id"),
            entity = EntityType.valueOf(rs.getString("entity")),
            entityId = rs.getLong("entity_id"),
            payload = rs.getString("payload"),
            status = OutboxStatus.valueOf(rs.getString("status")),
            createdAt = rs.getTimestamp("created_at").toInstant(),
            updatedAt = rs.getTimestamp("updated_at").toInstant()
        )

}