package org.bazar.space.adapter.inbound.kafka

import org.bazar.space.application.space.port.`in`.CompleteSpaceAvatarUploadUseCase
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(
    value = ["spring.kafka.topics.consumers.file-events.enabled"],
    havingValue = "true",
    matchIfMissing = false
)
class BazarFilesEventConsumer(
    private val completeSpaceAvatarUploadUseCase: CompleteSpaceAvatarUploadUseCase
) {

    @Value("\${file-domain}")
    private lateinit var fileDomain: String

    @KafkaListener(topics = ["\${spring.kafka.topics.consumers.file-events.name}"])
    fun consume(event: BazarFilesEvent) {
        if (event.domain != fileDomain) return
        completeSpaceAvatarUploadUseCase.execute(event.toCompleteSpaceAvatarUploadCommand())
    }
}