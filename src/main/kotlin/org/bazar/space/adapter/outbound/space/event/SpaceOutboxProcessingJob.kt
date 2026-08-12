package org.bazar.space.adapter.outbound.space.event

import org.bazar.space.infrastructure.config.kafka.KafkaProperties
import org.bazar.space.adapter.outbound.shared.outbox.OutboxRepositoryAdapter
import org.bazar.space.adapter.outbound.shared.job.AbstractOutboxJob
import org.bazar.space.adapter.outbound.space.kafka.KafkaSenderAdapter
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

    override val entityType = "SPACE"
    override val producerConfig by lazy {
        kafkaProperties.producers["space-events"]
            ?: throw IllegalStateException("No producer config found for space-events")
    }

    @Scheduled(fixedDelay = 2000)
    fun run() = process()

}