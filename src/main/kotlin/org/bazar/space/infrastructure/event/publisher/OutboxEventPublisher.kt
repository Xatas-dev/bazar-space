package org.bazar.space.infrastructure.event.publisher

import org.bazar.space.domain.event.SpaceDomainEvent
import org.bazar.space.infrastructure.event.SpaceEventDto
import org.bazar.space.infrastructure.event.SpaceEventType
import org.bazar.space.infrastructure.persistence.entity.Outbox
import org.bazar.space.infrastructure.persistence.entity.OutboxStatus
import org.bazar.space.infrastructure.persistence.repository.outbox.OutboxRepositoryAdapter
import org.bazar.space.domain.port.EventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import tools.jackson.databind.ObjectMapper

@Service
class OutboxEventPublisher(
    private val outboxRepository: OutboxRepositoryAdapter,
    private val objectMapper: ObjectMapper
) : EventPublisher {

    @Transactional(propagation = Propagation.MANDATORY)
    override fun publish(event: SpaceDomainEvent) {
        val dto = when (event) {
            is SpaceDomainEvent.Deleted -> SpaceEventDto(event.spaceId, SpaceEventType.DELETE)
        }
        val outbox = Outbox(
            entity = "SPACE",
            entityId = event.spaceId,
            payload = objectMapper.writeValueAsString(dto),
            status = OutboxStatus.NEW
        )
        outboxRepository.insertAll(listOf(outbox))
    }

}