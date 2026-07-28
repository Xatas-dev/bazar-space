package org.bazar.space.infrastructure.persistence.entity

import org.bazar.space.infrastructure.persistence.entity.OutboxStatus
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table(name = "outbox")
data class Outbox(
    val entity: String,
    val entityId: Long,
    val payload: String,
    var status: OutboxStatus,
    val createdAt: Instant = Instant.now(),
    @Id val id: Long? = null,
    var updatedAt: Instant = Instant.now()
)