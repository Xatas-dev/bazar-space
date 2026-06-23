package org.bazar.space.model.kafka.produce

data class SpaceEvent(
    val eventType: EventType,
    val spaceId: Long
)


enum class EventType {
    DELETE
}