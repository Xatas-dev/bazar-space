package org.bazar.space.application.joinrequest.port.out

import org.bazar.space.domain.joinrequest.SpaceJoinRequest
import java.util.UUID

interface SpaceJoinRequestRepositoryPort {
    fun save(request: SpaceJoinRequest): SpaceJoinRequest

    fun findAllBySpaceId(spaceId: Long): List<SpaceJoinRequest>

    fun deleteBySpaceIdAndUserId(spaceId: Long, userId: UUID)
}
