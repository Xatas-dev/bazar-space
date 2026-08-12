package org.bazar.space.adapter.outbound.userspace.persistence

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserSpaceEntityRepository : CrudRepository<UserSpaceEntity, Long> {

    fun findAllBySpaceId(spaceId: Long): List<UserSpaceEntity>

    fun deleteAllBySpaceId(spaceId: Long): Int

    fun deleteBySpaceIdAndUserId(spaceId: Long, userId: UUID): Int

    fun findBySpaceIdAndUserId(
        spaceId: Long,
        userId: UUID
    ): Optional<UserSpaceEntity>
}