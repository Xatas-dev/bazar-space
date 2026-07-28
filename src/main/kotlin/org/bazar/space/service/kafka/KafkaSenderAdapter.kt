package org.bazar.space.service.kafka

import org.apache.kafka.clients.producer.ProducerRecord
import org.bazar.space.util.exceptions.ApiException
import org.bazar.space.util.exceptions.ApiExceptions
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
                throw ApiException(ApiExceptions.KAFKA_SENDING_EXCEPTION, topic, payload, ex)
            }
    }

}