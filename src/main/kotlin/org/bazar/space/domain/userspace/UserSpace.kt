package org.bazar.space.domain.userspace

import java.time.Instant
import java.util.UUID

data class UserSpace(
    val id: Long? = null,
    val spaceId: Long,
    val userId: UUID,
    val creator: Boolean,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now()
) {

    companion object {
        fun createOwner(spaceId: Long, userId: UUID): UserSpace =
            UserSpace(spaceId = spaceId, userId = userId, creator = true)

        fun createMember(spaceId: Long, userId: UUID): UserSpace =
            UserSpace(spaceId = spaceId, userId = userId, creator = false)
    }
}
