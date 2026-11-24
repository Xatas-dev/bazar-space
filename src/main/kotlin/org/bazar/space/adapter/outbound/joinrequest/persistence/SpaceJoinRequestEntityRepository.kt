package org.bazar.space.adapter.outbound.joinrequest.persistence

import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Repository
interface SpaceJoinRequestEntityRepository : CrudRepository<SpaceJoinRequestEntity, Long> {

    @Transactional
    @Query(
        """
        INSERT INTO join_request (space_id, user_id, created_at, updated_at)
        VALUES (
            :#{#entity.spaceId},
            :#{#entity.userId},
            :#{T(java.sql.Timestamp).from(#entity.createdAt)},
            :#{T(java.sql.Timestamp).from(#entity.updatedAt)})
        ON CONFLICT (space_id, user_id) DO UPDATE SET space_id = EXCLUDED.space_id
        RETURNING *
    """
    )
    fun save(@Param("entity") entity: SpaceJoinRequestEntity): SpaceJoinRequestEntity

    fun findBySpaceIdAndUserId(spaceId: Long, userId: UUID): SpaceJoinRequestEntity?

    fun findAllBySpaceIdOrderByCreatedAt(spaceId: Long): List<SpaceJoinRequestEntity>

    fun deleteBySpaceIdAndUserId(spaceId: Long, userId: UUID): Int
}
