package org.bazar.space.service.events

import org.bazar.space.model.events.EntityEventWrapper

interface EventPublisher {

    fun publish(eventWrapper: EntityEventWrapper)

}