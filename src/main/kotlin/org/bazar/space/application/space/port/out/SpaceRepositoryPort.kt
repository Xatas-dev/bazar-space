package org.bazar.space.application.space.port.out

import org.bazar.space.domain.space.Space
import java.util.UUID

interface SpaceRepositoryPort {
    fun save(space: Space): Space

    fun findById(id: Long): Space?

    fun deleteById(id: Long)

    fun findAllByUserId(userId: UUID): List<Space>
}
