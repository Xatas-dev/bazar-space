package org.bazar.space.adapter.outbound.userspace.persistence

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.*

@Table(name = "user_space")
class UserSpaceEntity(
    val spaceId: Long,
    val userId: UUID,
    val creator: Boolean,
    @Id val id: Long? = null,
    val createdAt: Instant = Instant.now(),
    var updatedAt: Instant = Instant.now()
)