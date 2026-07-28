package org.bazar.space.model.events

data class EntityEventWrapper(
    val entityId: Long,
    val entity: EntityType,
    val event: Any
)
