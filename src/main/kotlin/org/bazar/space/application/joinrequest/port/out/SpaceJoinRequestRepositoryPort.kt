package org.bazar.space.application.joinrequest.port.out

import org.bazar.space.application.shared.PageResult
import org.bazar.space.domain.joinrequest.SpaceJoinRequest
import java.util.UUID

interface SpaceJoinRequestRepositoryPort {
    fun save(request: SpaceJoinRequest): SpaceJoinRequest

    fun findAllBySpaceId(spaceId: Long): List<SpaceJoinRequest>

    fun findBySpaceId(spaceId: Long, limit: Int): List<SpaceJoinRequest>

    fun findPageBySpaceId(spaceId: Long, page: Int, size: Int): PageResult<SpaceJoinRequest>

    fun findAllBySpaceIdAndIds(spaceId: Long, requestIds: Collection<Long>): List<SpaceJoinRequest>

    fun deleteBySpaceIdAndIds(spaceId: Long, requestIds: Collection<Long>): Int

    fun deleteBySpaceIdAndUserId(spaceId: Long, userId: UUID)
}
