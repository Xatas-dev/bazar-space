package org.bazar.space.domain.joinrequest

import java.time.Instant
import java.util.UUID

data class SpaceJoinRequest(
    val id: Long? = null,
    val spaceId: Long,
    val userId: UUID,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now()
) {

    companion object {
        fun create(spaceId: Long, userId: UUID): SpaceJoinRequest =
            SpaceJoinRequest(spaceId = spaceId, userId = userId)
    }
}
