package org.bazar.space.service.events.impl

import org.bazar.space.model.events.EntityEventWrapper
import org.bazar.space.persistence.entity.Outbox
import org.bazar.space.persistence.entity.enums.OutboxStatus
import org.bazar.space.persistence.repository.OutboxRepository
import org.bazar.space.service.events.EventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import tools.jackson.databind.ObjectMapper

@Service
class OutboxEventPublisher(
    private val outboxRepository: OutboxRepository,
    private val objectMapper: ObjectMapper
) : EventPublisher {

    @Transactional(propagation = Propagation.MANDATORY)
    override fun publish(eventWrapper: EntityEventWrapper) {
        val outbox = Outbox(
            entity = eventWrapper.entity,
            entityId = eventWrapper.entityId,
            payload = objectMapper.writeValueAsString(eventWrapper.event),
            status = OutboxStatus.NEW
        )
        outboxRepository.insertAll(listOf(outbox))
    }

}