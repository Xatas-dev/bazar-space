package org.bazar.space.infrastructure.persistence.repository

import org.bazar.space.infrastructure.persistence.entity.SpaceEntity
import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SpaceEntityRepository : CrudRepository<SpaceEntity, Long> {

    @Query(
        """
        SELECT s.* FROM space s
        JOIN user_space us ON us.space_id = s.id
        WHERE us.user_id = :userId
    """
    )
    fun findAllByUserId(userId: UUID): List<SpaceEntity>

    @Modifying
    @Query("DELETE FROM space WHERE id = :spaceId")
    fun deleteBySpaceId(spaceId: Long): Int
}