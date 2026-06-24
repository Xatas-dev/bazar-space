package org.bazar.space.service.kafka

import org.bazar.space.model.kafka.produce.SpaceEvent

interface SpaceEventPublisher {

    fun send(event: SpaceEvent)

}