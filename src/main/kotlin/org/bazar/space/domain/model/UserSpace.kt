package org.bazar.space.domain.model

import java.time.Instant
import java.util.UUID

data class UserSpace(
    val id: Long? = null,
    val spaceId: Long,
    val userId: UUID,
    val creator: Boolean,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now()
)
