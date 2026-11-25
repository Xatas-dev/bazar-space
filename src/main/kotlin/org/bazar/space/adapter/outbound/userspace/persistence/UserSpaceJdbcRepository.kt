package org.bazar.space.adapter.outbound.userspace.persistence

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Repository
import java.sql.Timestamp

@Repository
class UserSpaceJdbcRepository(
    private val jdbcOperations: NamedParameterJdbcOperations
) {

    companion object {
        private val INSERT_ALL_IGNORING_EXISTING = """
            insert into user_space (space_id, user_id, creator, created_at, updated_at)
            values (:space_id, :user_id, :creator, :created_at, :updated_at)
            on conflict (space_id, user_id) do nothing
        """.trimIndent()
    }

    fun insertAllIgnoringExisting(entities: Collection<UserSpaceEntity>) {
        if (entities.isEmpty()) return
        val batchParams = entities.map { userSpace ->
            MapSqlParameterSource()
                .addValue("space_id", userSpace.spaceId)
                .addValue("user_id", userSpace.userId)
                .addValue("creator", userSpace.creator)
                .addValue("created_at", Timestamp.from(userSpace.createdAt))
                .addValue("updated_at", Timestamp.from(userSpace.updatedAt))
        }.toTypedArray()

        jdbcOperations.batchUpdate(INSERT_ALL_IGNORING_EXISTING, batchParams)
    }
}
