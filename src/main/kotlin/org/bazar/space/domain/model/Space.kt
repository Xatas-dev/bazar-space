package org.bazar.space.domain.model

import java.time.Instant

data class Space(
    val id: Long? = null,
    val name: String,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now()
)
