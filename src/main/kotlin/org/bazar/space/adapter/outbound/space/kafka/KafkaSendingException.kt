package org.bazar.space.adapter.outbound.space.kafka

class KafkaSendingException(
    topic: String,
    payload: String,
    cause: Throwable? = null
) : RuntimeException(
    "Couldn't produce message to kafka topic: $topic a record: $payload",
    cause
)