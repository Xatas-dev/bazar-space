package org.bazar.space.service.kafka.impl

import io.github.oshai.kotlinlogging.KotlinLogging
import org.bazar.space.config.kafka.KafkaProperties
import org.bazar.space.model.kafka.produce.SpaceEvent
import org.bazar.space.service.kafka.KafkaSenderAdapter
import org.bazar.space.service.kafka.SpaceEventPublisher
import org.springframework.stereotype.Component

@Component
class SpaceEventKafkaPublisher(
    private val kafkaProducerAdapter: KafkaSenderAdapter,
    private val kafkaProperties: KafkaProperties
) : SpaceEventPublisher {
    private val logger = KotlinLogging.logger { }

    private val topicProducerConfig by lazy {
        kafkaProperties.producers["space-events"]
            ?: throw IllegalStateException("space-events kafka config missing")
    }

    override fun send(event: SpaceEvent) {
        if (!topicProducerConfig.enabled) {
            logger.warn { "Unable to send to topic ${topicProducerConfig.name}, it's disabled" }
            return
        }

        kafkaProducerAdapter.send(topicProducerConfig.name, event.spaceId.toString(), event)
    }

}