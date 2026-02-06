package org.bazar.space.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.*

@Table(name = "user_space")
class UserSpace(
    val spaceId: Long,
    val userId: UUID,
    @Id val id: Long? = null,
    val createdAt: Instant = Instant.now(),
    var updatedAt: Instant = Instant.now()
)