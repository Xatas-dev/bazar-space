package org.bazar.space.adapter.inbound.kafka

import org.assertj.core.api.Assertions.assertThat
import org.awaitility.kotlin.atMost
import org.awaitility.kotlin.await
import org.awaitility.kotlin.untilAsserted
import org.awaitility.kotlin.withPollInterval
import org.bazar.space.BaseIntegrationTest
import org.bazar.space.adapter.outbound.space.persistence.SpaceAvatarEntityRepository
import org.bazar.space.application.space.port.out.files.FileDownloadLinkDto
import org.bazar.space.application.space.port.out.files.FileStoragePort
import org.bazar.space.domain.space.SpaceAvatarStatus
import org.bazar.space.utils.SpaceAvatarCreator
import org.bazar.space.utils.SpaceCreator
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.test.context.bean.override.mockito.MockitoBean
import tools.jackson.databind.ObjectMapper
import java.time.Duration
import java.util.UUID

class BazarFilesEventConsumerTest : BaseIntegrationTest() {

    @Autowired
    private lateinit var kafkaTemplate: KafkaTemplate<String, String>

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var spaceCreator: SpaceCreator

    @Autowired
    private lateinit var spaceAvatarCreator: SpaceAvatarCreator

    @Autowired
    private lateinit var spaceAvatarEntityRepository: SpaceAvatarEntityRepository

    @MockitoBean
    private lateinit var fileStoragePort: FileStoragePort

    @Value("\${spring.kafka.topics.consumers.file-events.name}")
    private lateinit var filesEventTopic: String

    private fun insertPendingAvatar(spaceId: Long, fileId: UUID) {
        spaceAvatarCreator.create(
            spaceId = spaceId,
            fileId = fileId,
            isActive = false,
            status = SpaceAvatarStatus.PENDING.name,
            url = null,
        )
    }

    private fun uploadedEvent(fileId: UUID) = BazarFilesEvent(
        domain = "bazar-space",
        size = 1234L,
        fileUuid = fileId,
        status = FileUploadStatus.UPLOADED,
        fileName = "avatar.png",
        contentType = "image/png",
        errors = emptyList()
    )

    private fun failedEvent(fileId: UUID, errors: List<FileUploadError>) =
        BazarFilesEvent(
            domain = "bazar-space",
            size = 1234L,
            fileUuid = fileId,
            status = FileUploadStatus.ERROR,
            fileName = "avatar.png",
            contentType = "image/png",
            errors = errors
        )

    private fun send(event: BazarFilesEvent) {
        kafkaTemplate.send(filesEventTopic, objectMapper.writeValueAsString(event)).get()
    }

    @Test
    fun consumeFileEvent_happyPath_marksAvatarSuccess() {
        val space = spaceCreator.create()
        val fileId = UUID.randomUUID()
        val expectedUrl = "https://s3.bazar/$fileId"

        insertPendingAvatar(space.id!!, fileId)

        whenever(fileStoragePort.getDownloadLink(any()))
            .thenReturn(FileDownloadLinkDto(expectedUrl))

        send(uploadedEvent(fileId))

        await atMost Duration.ofSeconds(30) withPollInterval Duration.ofMillis(500) untilAsserted {
            val avatar = spaceAvatarEntityRepository.findBySpaceIdAndIsActive(space.id, true)
            assertThat(avatar).isNotNull
            assertThat(avatar!!.status).isEqualTo(SpaceAvatarStatus.SUCCESS.name)
            assertThat(avatar.url).isEqualTo(expectedUrl)
            assertThat(avatar.isActive).isTrue()
        }
    }

    @Test
    fun consumeFileEvent_ignoresEventForOtherDomain() {
        val space = spaceCreator.create()
        val fileId = UUID.randomUUID()

        insertPendingAvatar(space.id!!, fileId)

        send(uploadedEvent(fileId).copy(domain = "some-other-domain"))

        Thread.sleep(3_000)

        verify(fileStoragePort, never()).getDownloadLink(any())

        val stored = spaceAvatarEntityRepository.findByFileId(fileId)
        assertThat(stored).isNotNull
        assertThat(stored!!.status).isEqualTo(SpaceAvatarStatus.PENDING.name)
        assertThat(stored.isActive).isFalse()
    }

    @Test
    fun consumeFileEvent_uploadFailed_marksAvatarFailed() {
        val space = spaceCreator.create()
        val fileId = UUID.randomUUID()

        insertPendingAvatar(space.id!!, fileId)

        send(failedEvent(fileId, listOf(FileUploadError("INVALID_EXTENSION", "Not supported file extension"))))

        await atMost Duration.ofSeconds(30) withPollInterval Duration.ofMillis(500) untilAsserted {
            val avatar = spaceAvatarEntityRepository.findByFileId(fileId)
            assertThat(avatar).isNotNull
            assertThat(avatar!!.status).isEqualTo(SpaceAvatarStatus.ERROR.name)
        }

        val stored = spaceAvatarEntityRepository.findByFileId(fileId)!!
        assertThat(stored.isActive).isFalse()
        verify(fileStoragePort, never()).getDownloadLink(any())
    }
}