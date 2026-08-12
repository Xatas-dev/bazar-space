package org.bazar.space.adapter.outbound.space.event

import org.bazar.space.adapter.outbound.shared.outbox.OutboxEntity
import org.bazar.space.adapter.outbound.shared.outbox.OutboxRepositoryAdapter
import org.bazar.space.adapter.outbound.shared.outbox.OutboxStatus
import org.bazar.space.application.shared.port.out.EventPublisher
import org.bazar.space.domain.space.SpaceDomainEvent
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionSynchronizationManager
import tools.jackson.databind.ObjectMapper

@Service
class OutboxEventPublisher(
    private val outboxRepository: OutboxRepositoryAdapter,
    private val objectMapper: ObjectMapper
) : EventPublisher {

    override fun publish(event: SpaceDomainEvent) {
        require(TransactionSynchronizationManager.isActualTransactionActive()) {
            "OutboxEventPublisher.publish() must be called within an active transaction"
        }
        val dto = when (event) {
            is SpaceDomainEvent.Deleted -> SpaceEventDto(event.spaceId, SpaceEventType.DELETE)
        }
        val outbox = OutboxEntity(
            entity = "SPACE",
            entityId = event.spaceId,
            payload = objectMapper.writeValueAsString(dto),
            status = OutboxStatus.NEW
        )
        outboxRepository.insertAll(listOf(outbox))
    }

}