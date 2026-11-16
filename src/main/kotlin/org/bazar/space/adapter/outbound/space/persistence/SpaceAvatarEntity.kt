package org.bazar.space.adapter.outbound.space.persistence

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.UUID

@Table(name = "space_avatar")
data class SpaceAvatarEntity(
    val spaceId: Long,
    val fileId: UUID,
    var isActive: Boolean,
    var status: String,
    var url: String?,
    var errorReason: String?,
    val createdAt: Instant,
    var updatedAt: Instant,
    @Id
    val id: Long? = null
)