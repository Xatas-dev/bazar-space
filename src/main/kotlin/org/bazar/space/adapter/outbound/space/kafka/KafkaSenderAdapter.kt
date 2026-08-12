package org.bazar.space.adapter.outbound.space.kafka

import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

@Component
class KafkaSenderAdapter(
    private val kafkaTemplate: KafkaTemplate<String, String>
) {

    fun send(
        topic: String,
        key: String,
        payload: String,
        headers: Map<String, String> = emptyMap()
    ): CompletableFuture<SendResult<String, String>> {
        val producerRecord = ProducerRecord(topic, null, key, payload)

        headers.forEach { (key, value) ->
            producerRecord.headers().add(key, value.toByteArray())
        }

        return kafkaTemplate.send(producerRecord)
            .orTimeout(3L, TimeUnit.SECONDS)
            .exceptionally { ex ->
                throw KafkaSendingException(topic, payload, ex)
            }
    }

}