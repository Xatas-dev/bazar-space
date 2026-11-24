package org.bazar.space.adapter.outbound.joinrequest.persistence

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.UUID

@Table(name = "join_request")
data class SpaceJoinRequestEntity(
    val spaceId: Long,
    val userId: UUID,
    @Id
    val id: Long? = null,
    val createdAt: Instant = Instant.now(),
    var updatedAt: Instant = Instant.now()
)
