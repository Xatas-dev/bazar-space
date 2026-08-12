package org.bazar.space.infrastructure.config.kafka

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.kafka.topics")
data class KafkaProperties(
    val producers: Map<String, ProducerProperties>
)

data class ProducerProperties(
    val name: String,
    val enabled: Boolean
)

