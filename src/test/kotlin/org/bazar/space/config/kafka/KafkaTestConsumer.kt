package org.bazar.space.infrastructure.config.kafka

import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.bazar.space.infrastructure.config.SharedAppContext
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer
import org.springframework.kafka.test.utils.KafkaTestUtils.consumerProps
import java.time.Duration
import java.util.concurrent.CopyOnWriteArrayList

class KafkaTestConsumer<V : Any>(
    topic: String,
    valueType: Class<V>
) {
    private val records = CopyOnWriteArrayList<ConsumerRecord<String, V>>()


    private val consumer: Consumer<String, V>

    init {
        val consumerProps = consumerProps(SharedAppContext.kafka.bootstrapServers, "$topic.test.0")
        consumerProps["auto.offset.reset"] = "earliest"

        val keyDeserializer = StringDeserializer()

        val valueDeserializer = JacksonJsonDeserializer(valueType).apply {
            addTrustedPackages("*")
            setUseTypeHeaders(false)
        }

        val cf = DefaultKafkaConsumerFactory<String, V>(consumerProps, keyDeserializer, valueDeserializer)
        consumer = cf.createConsumer()
        consumer.subscribe(listOf(topic))
    }

    fun poll(): List<ConsumerRecord<String, V>> {
        val polledRecords = consumer.poll(Duration.ofSeconds(30))
        records.addAll(polledRecords)
        return records
    }

}