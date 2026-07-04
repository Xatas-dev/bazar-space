package org.bazar.space.service.events.impl

import org.bazar.space.config.kafka.KafkaProperties
import org.bazar.space.model.events.EntityType
import org.bazar.space.persistence.repository.adapter.OutboxRepositoryAdapter
import org.bazar.space.service.events.AbstractOutboxJob
import org.bazar.space.service.kafka.KafkaSenderAdapter
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(value = ["app.jobs.space-outbox.enabled"], havingValue = "true")
class SpaceOutboxProcessingJob(
    outboxRepositoryAdapter: OutboxRepositoryAdapter,
    kafkaSenderAdapter: KafkaSenderAdapter,
    kafkaProperties: KafkaProperties,
) : AbstractOutboxJob(outboxRepositoryAdapter, kafkaSenderAdapter, kafkaProperties) {

    override val entityType = EntityType.SPACE
    override val producerConfig by lazy {
        kafkaProperties.producers["space-events"]
            ?: throw IllegalStateException("No producer config found for space-events")
    }

    @Scheduled(fixedDelay = 2000)
    fun run() = process()

}