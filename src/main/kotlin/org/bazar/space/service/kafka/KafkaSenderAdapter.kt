package org.bazar.space.service.kafka

import org.apache.kafka.clients.producer.ProducerRecord
import org.bazar.space.util.exceptions.ApiException
import org.bazar.space.util.exceptions.ApiExceptions
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.resilience.annotation.Retryable
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class KafkaSenderAdapter(
    private val kafkaTemplate: KafkaTemplate<String, Any>
) {

    @Retryable(includes = [ApiException::class], maxRetries = 3, delay = 1000)
    fun send(
        topic: String,
        key: String,
        event: Any,
        headers: Map<String, String> = emptyMap()
    ) {
        val producerRecord = ProducerRecord(topic, null, key, event)

        headers.forEach { (key, value) ->
            producerRecord.headers().add(key, value.toByteArray())
        }

        kafkaTemplate.send(producerRecord)
            .orTimeout(3L, TimeUnit.SECONDS)
            .exceptionally { ex ->
                throw ApiException(ApiExceptions.KAFKA_SENDING_EXCEPTION, topic, event, ex)
            }.join()
    }

}