package org.bazar.space.adapter.outbound.space.event

data class SpaceEventDto(
    val spaceId: Long,
    val type: SpaceEventType
)

enum class SpaceEventType {
    DELETE
}
