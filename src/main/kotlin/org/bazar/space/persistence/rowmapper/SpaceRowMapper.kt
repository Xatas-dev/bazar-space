package org.bazar.space.persistence.rowmapper

import org.bazar.space.persistence.entity.Space
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class SpaceRowMapper :
    RowMapper<Space> {

    override fun mapRow(rs: ResultSet, rowNum: Int): Space =
        Space(
            id = rs.getLong("id"),
            name = rs.getString("name"),
            createdAt = rs.getTimestamp("created_at").toInstant(),
            updatedAt = rs.getTimestamp("updated_at").toInstant()
        )

}