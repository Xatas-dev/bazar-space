package org.bazar.space.infrastructure.event

data class SpaceEventDto(
    val spaceId: Long,
    val type: SpaceEventType
)

enum class SpaceEventType {
    DELETE
}
