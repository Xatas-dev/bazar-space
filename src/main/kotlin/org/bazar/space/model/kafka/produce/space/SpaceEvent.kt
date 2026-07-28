package org.bazar.space.model.kafka.produce.space

data class SpaceEvent(
    val spaceId: Long,
    val type: SpaceEventType
)