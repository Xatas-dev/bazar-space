package org.bazar.space.adapter.outbound.joinrequest.persistence

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Repository
interface SpaceJoinRequestEntityRepository :
    CrudRepository<SpaceJoinRequestEntity, Long>,
    PagingAndSortingRepository<SpaceJoinRequestEntity, Long> {

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

    @Query("SELECT * FROM join_request WHERE space_id = :spaceId ORDER BY id LIMIT :limit")
    fun findBySpaceId(spaceId: Long, limit: Int): List<SpaceJoinRequestEntity>

    fun findAllBySpaceId(spaceId: Long, pageable: Pageable): Page<SpaceJoinRequestEntity>

    fun findAllBySpaceIdAndIdIn(spaceId: Long, ids: Collection<Long>): List<SpaceJoinRequestEntity>

    @Transactional
    @Modifying
    @Query("DELETE FROM join_request WHERE space_id = :spaceId AND id IN (:requestIds)")
    fun deleteBySpaceIdAndIds(spaceId: Long, requestIds: Collection<Long>): Int

    @Transactional
    @Modifying
    @Query(
        """
        DELETE FROM join_request
        WHERE id IN (SELECT id FROM join_request WHERE space_id = :spaceId ORDER BY id LIMIT :limit)
    """
    )
    fun deleteBatchBySpaceId(spaceId: Long, limit: Int): Int

    fun findBySpaceIdAndUserId(spaceId: Long, userId: UUID): SpaceJoinRequestEntity?

    fun findAllBySpaceIdOrderByCreatedAt(spaceId: Long): List<SpaceJoinRequestEntity>

    fun deleteBySpaceIdAndUserId(spaceId: Long, userId: UUID): Int
}
