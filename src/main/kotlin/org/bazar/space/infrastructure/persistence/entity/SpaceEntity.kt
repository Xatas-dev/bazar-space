package org.bazar.space.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table(name = "space")
data class SpaceEntity(
    @Id
    val id: Long? = null,

    var name: String,

    val createdAt: Instant = Instant.now(),

    var updatedAt: Instant = Instant.now()
)