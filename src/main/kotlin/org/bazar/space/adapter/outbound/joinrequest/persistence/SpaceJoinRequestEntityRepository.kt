package org.bazar.space.adapter.outbound.joinrequest.persistence

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface SpaceJoinRequestEntityRepository : CrudRepository<SpaceJoinRequestEntity, Long> {

    fun findAllBySpaceIdOrderByCreatedAt(spaceId: Long): List<SpaceJoinRequestEntity>

    fun deleteBySpaceIdAndUserId(spaceId: Long, userId: UUID): Int
}
