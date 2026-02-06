package org.bazar.space.persistence.repository

import org.bazar.space.persistence.entity.Space
import org.bazar.space.persistence.rowmapper.SpaceRowMapper
import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SpaceRepository : CrudRepository<Space, Long> {

    @Query("""
        SELECT s.* FROM space s
        JOIN user_space us ON us.space_id = s.id
        WHERE us.user_id = :userId
    """)
    fun findAllByUserId(userId: UUID): List<Space>

    // deleteById is already provided by CrudRepository
    // But if you need custom delete logic:
    @Modifying
    @Query("DELETE FROM space WHERE id = :spaceId")
    fun deleteBySpaceId(spaceId: Long): Int
}