package org.bazar.space.adapter.outbound.shared.outbox

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class OutboxRowMapper :
    RowMapper<OutboxEntity> {

    override fun mapRow(rs: ResultSet, rowNum: Int): OutboxEntity =
        OutboxEntity(
            id = rs.getLong("id"),
            entity = rs.getString("entity"),
            entityId = rs.getLong("entity_id"),
            payload = rs.getString("payload"),
            status = OutboxStatus.valueOf(rs.getString("status")),
            createdAt = rs.getTimestamp("created_at").toInstant(),
            updatedAt = rs.getTimestamp("updated_at").toInstant()
        )

}