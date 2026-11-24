package org.bazar.space.utils

import org.bazar.space.adapter.outbound.space.persistence.SpaceEntity
import org.bazar.space.adapter.outbound.space.persistence.SpaceEntityRepository
import org.bazar.space.domain.shared.RandomGenerator
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class SpaceCreator(
    private val spaceEntityRepository: SpaceEntityRepository
) {
    fun create(
        name: String = "Space ${UUID.randomUUID()}",
        joinCode: String = RandomGenerator.urlSafeToken(),
    ): SpaceEntity = spaceEntityRepository.save(
        SpaceEntity(
            name = name,
            joinCode = joinCode,
        )
    )
}
