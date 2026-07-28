package org.bazar.space.utils

import org.bazar.space.infrastructure.persistence.entity.SpaceEntity
import org.bazar.space.infrastructure.persistence.repository.SpaceEntityRepository
import org.springframework.stereotype.Component

@Component
class SpaceCreator(
    val spaceEntityRepository: SpaceEntityRepository
) {
    fun create(action: SpaceBuilder.() -> Unit = {}): SpaceEntity {
        val builder = SpaceBuilder()
        builder.action()
        return spaceEntityRepository.save(builder.build())
    }

    class SpaceBuilder {
        var name: String = "Dota 2"

        fun build(): SpaceEntity {
            return SpaceEntity(
                name = name
            )
        }
    }
}
