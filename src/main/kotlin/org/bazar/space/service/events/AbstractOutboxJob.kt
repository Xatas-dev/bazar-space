package org.bazar.space.service.events

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import org.bazar.space.config.kafka.KafkaProperties
import org.bazar.space.config.kafka.ProducerProperties
import org.bazar.space.model.events.EntityType
import org.bazar.space.persistence.entity.enums.OutboxStatus
import org.bazar.space.persistence.repository.adapter.OutboxRepositoryAdapter
import org.bazar.space.service.kafka.KafkaSenderAdapter
import java.time.Instant

abstract class AbstractOutboxJob(
    private val outboxRepositoryAdapter: OutboxRepositoryAdapter,
    private val kafkaSenderAdapter: KafkaSenderAdapter,
    private val kafkaProperties: KafkaProperties
) {

    abstract val entityType: EntityType
    abstract val producerConfig: ProducerProperties

    private val log by lazy { logger("${entityType.name}-outbox-job") }


    fun process() {
        if (!producerConfig.enabled) {
            log.warn { "${producerConfig.name} is disabled, skipping outboxes" }
            return
        }

        val resultingOutboxes = outboxRepositoryAdapter.findAndLockNextBatchByEntity(entityType).map {
            it to kafkaSenderAdapter.send(producerConfig.name, it.entityId.toString(), it.payload)
        }.map { (outbox, future) ->
            runCatching { future.join() }
                .onSuccess { outbox.status = OutboxStatus.DONE }
                .onFailure {
                    log.warn(it) { "Failed to send outbox ${outbox.entityId}" }
                    outbox.status = OutboxStatus.ERROR
                }
            outbox.updatedAt = Instant.now()
            return@map outbox
        }

        outboxRepositoryAdapter.updateAllSetStatus(resultingOutboxes)
    }

}