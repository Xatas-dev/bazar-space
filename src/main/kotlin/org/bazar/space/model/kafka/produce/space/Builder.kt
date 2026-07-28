package org.bazar.space.model.kafka.produce.space

import org.bazar.space.model.events.EntityEventWrapper
import org.bazar.space.model.events.EntityType

fun buildSpaceEvent(spaceId: Long, eventType: SpaceEventType): EntityEventWrapper {
    return EntityEventWrapper(
        entityId = spaceId,
        entity = EntityType.SPACE,
        event = SpaceEvent(
            spaceId = spaceId,
            type = eventType
        )
    )
}