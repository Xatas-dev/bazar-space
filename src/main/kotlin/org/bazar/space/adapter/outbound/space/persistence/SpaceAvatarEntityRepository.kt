package org.bazar.space.adapter.outbound.space.persistence

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface SpaceAvatarEntityRepository : CrudRepository<SpaceAvatarEntity, Long> {

    fun findByFileId(fileId: UUID): SpaceAvatarEntity?

    fun findBySpaceIdAndIsActive(spaceId: Long, isActive: Boolean): SpaceAvatarEntity?

    fun findBySpaceIdInAndIsActive(spaceIds: Collection<Long>, isActive: Boolean): List<SpaceAvatarEntity>

    fun findBySpaceIdOrderByCreatedAtDesc(spaceId: Long): List<SpaceAvatarEntity>

    fun deleteAllBySpaceId(spaceId: Long)
}