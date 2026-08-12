package org.bazar.space.application.shared.port.out

import org.bazar.space.domain.space.SpaceDomainEvent

fun interface EventPublisher {
    fun publish(event: SpaceDomainEvent)
}
