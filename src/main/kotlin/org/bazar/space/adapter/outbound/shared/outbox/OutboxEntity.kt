package org.bazar.space.adapter.outbound.shared.outbox

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table(name = "outbox")
data class OutboxEntity(
    val entity: String,
    val entityId: Long,
    val payload: String,
    var status: OutboxStatus,
    val createdAt: Instant = Instant.now(),
    @Id val id: Long? = null,
    var updatedAt: Instant = Instant.now()
)