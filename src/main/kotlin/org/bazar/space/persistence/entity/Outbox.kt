package org.bazar.space.persistence.entity

import org.bazar.space.model.events.EntityType
import org.bazar.space.persistence.entity.enums.OutboxStatus
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table(name = "outbox")
data class Outbox(
    val entity: EntityType,
    val entityId: Long,
    val payload: String,
    var status: OutboxStatus,
    val createdAt: Instant = Instant.now(),
    @Id val id: Long? = null,
    var updatedAt: Instant = Instant.now()
)