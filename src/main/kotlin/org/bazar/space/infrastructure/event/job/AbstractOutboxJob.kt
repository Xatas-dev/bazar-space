package org.bazar.space.infrastructure.event.job

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import org.bazar.space.config.kafka.KafkaProperties
import org.bazar.space.config.kafka.ProducerProperties
import org.bazar.space.infrastructure.persistence.entity.OutboxStatus
import org.bazar.space.infrastructure.persistence.repository.outbox.OutboxRepositoryAdapter
import org.bazar.space.infrastructure.kafka.KafkaSenderAdapter
import java.time.Instant

abstract class AbstractOutboxJob(
    private val outboxRepositoryAdapter: OutboxRepositoryAdapter,
    private val kafkaSenderAdapter: KafkaSenderAdapter,
    private val kafkaProperties: KafkaProperties
) {

    abstract val entityType: String
    abstract val producerConfig: ProducerProperties

    private val log by lazy { logger("${entityType.lowercase()}-outbox-job") }


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