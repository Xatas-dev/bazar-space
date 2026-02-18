package org.bazar.space.persistence.rowmapper

import org.bazar.space.persistence.entity.UserSpace
import org.bazar.space.util.extension.toUuid
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class UserSpaceRowMapper :
    RowMapper<UserSpace> {

    override fun mapRow(rs: ResultSet, rowNum: Int): UserSpace =
        UserSpace(
            id = rs.getLong("id"),
            createdAt = rs.getTimestamp("created_at").toInstant(),
            updatedAt = rs.getTimestamp("updated_at").toInstant(),
            spaceId = rs.getLong("space_id"),
            userId = rs.getString("userId").toUuid()
        )

}