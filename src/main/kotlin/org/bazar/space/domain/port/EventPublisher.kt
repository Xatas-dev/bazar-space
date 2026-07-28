package org.bazar.space.domain.port

import org.bazar.space.domain.event.SpaceDomainEvent

fun interface EventPublisher {
    fun publish(event: SpaceDomainEvent)
}
