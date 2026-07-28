package org.bazar.space.domain.port

import org.bazar.space.domain.model.Space
import java.util.UUID

interface SpaceRepositoryPort {
    fun save(space: Space): Space

    fun findById(id: Long): Space?

    fun deleteById(id: Long)

    fun findAllByUserId(userId: UUID): List<Space>
}
