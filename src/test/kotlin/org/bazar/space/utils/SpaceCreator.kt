package org.bazar.space.infrastructure.utils

import org.bazar.space.adapter.outbound.space.persistence.SpaceEntity
import org.bazar.space.adapter.outbound.space.persistence.SpaceEntityRepository
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
