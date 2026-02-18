package org.bazar.space.utils

import org.bazar.space.persistence.entity.Space
import org.bazar.space.persistence.repository.SpaceRepository
import org.springframework.stereotype.Component

@Component
class SpaceCreator(
    val spaceRepository: SpaceRepository
) {
    fun create(action: SpaceBuilder.() -> Unit = {}): Space {
        val builder = SpaceBuilder()
        builder.action()
        return spaceRepository.save(builder.build())
    }

    class SpaceBuilder {
        var name: String = "Dota 2"

        fun build(): Space {
            return Space(
                name = name
            )
        }
    }
}