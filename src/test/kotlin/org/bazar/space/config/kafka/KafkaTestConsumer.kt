package org.bazar.space.config.kafka

import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.bazar.space.config.SharedAppContext
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer
import org.springframework.kafka.test.utils.KafkaTestUtils.consumerProps
import java.time.Duration
import java.util.concurrent.CopyOnWriteArrayList

class KafkaTestConsumer<K : Any, V : Any>(
    topic: String
) {
    private val records = CopyOnWriteArrayList<ConsumerRecord<K, V>>()


    private val consumer: Consumer<K, V>

    init {
        val consumerProps = consumerProps(SharedAppContext.kafka.bootstrapServers, "$topic.test.0")
        consumerProps["auto.offset.reset"] = "earliest"
        consumerProps[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        consumerProps[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = JacksonJsonDeserializer::class.java
        consumerProps[JacksonJsonDeserializer.TRUSTED_PACKAGES] = "*"
        val cf = DefaultKafkaConsumerFactory<K, V>(consumerProps)
        consumer = cf.createConsumer()
        consumer.subscribe(listOf(topic))
    }

    fun poll(): List<ConsumerRecord<K, V>> {
        val polledRecords = consumer.poll(Duration.ofSeconds(30))
        records.addAll(polledRecords)
        return records
    }

}