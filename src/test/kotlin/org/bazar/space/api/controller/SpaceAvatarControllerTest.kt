package org.bazar.space.api.controller

import org.assertj.core.api.Assertions.assertThat
import org.bazar.space.BaseWebTest
import org.bazar.space.adapter.inbound.rest.error.ApiErrorResponse
import org.bazar.space.adapter.outbound.space.persistence.SpaceAvatarEntityRepository
import org.bazar.space.application.space.port.out.files.FileStoragePort
import org.bazar.space.application.space.port.out.files.FileUploadLinkDto
import org.bazar.space.domain.space.SpaceAvatarStatus
import org.bazar.space.utils.SpaceAvatarCreator
import org.bazar.space.utils.SpaceCreator
import org.bazar.space.model.AvatarUploadTicketDtoResponse
import org.bazar.space.model.GetSpaceAvatarsResponse
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import tools.jackson.databind.ObjectMapper
import java.util.UUID

class SpaceAvatarControllerTest : BaseWebTest() {

    @Autowired
    lateinit var spaceCreator: SpaceCreator

    @Autowired
    lateinit var spaceAvatarCreator: SpaceAvatarCreator

    @Autowired
    lateinit var spaceAvatarEntityRepository: SpaceAvatarEntityRepository

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockitoBean
    lateinit var fileStoragePort: FileStoragePort

    @Test
    @DisplayName("Initiate avatar upload returns avatar id and upload url and persists PENDING avatar")
    fun initiateAvatarUpload_shouldReturnAvatarIdAndUploadUrlAndPersistPending() {
        val space = spaceCreator.create()
        val uploadUrl = "https://s3.bazar/upload"
        val fileId = UUID.randomUUID()

        whenever(fileStoragePort.initiateUpload(any()))
            .thenReturn(FileUploadLinkDto(uploadUrl, fileId))

        val actualResponse = mockMvc.post("/space/${space.id}/avatar") {
            accept = APPLICATION_JSON
            contentType = APPLICATION_JSON
            content = """{"fileName":"avatar.png","contentType":"image/png","sizeBytes":1234}"""
        }.andExpect {
            status { is2xxSuccessful() }
        }.andReturn().response.contentAsString

        val dto = objectMapper.readValue(actualResponse, AvatarUploadTicketDtoResponse::class.java)

        val avatars = spaceAvatarEntityRepository.findAll()
        assertThat(avatars).hasSize(1)
        val avatar = avatars.first()

        assertThat(dto.uploadUrl).isEqualTo(uploadUrl)
        assertThat(dto.avatarId).isEqualTo(avatar.id)
        assertThat(avatar.spaceId).isEqualTo(space.id)
        assertThat(avatar.fileId).isEqualTo(fileId)
        assertThat(avatar.status).isEqualTo(SpaceAvatarStatus.PENDING.name)
        assertThat(avatar.isActive).isFalse()
    }

    @Test
    @DisplayName("Get space avatars returns all avatars with id, status, url and createdAt")
    fun getSpaceAvatar_shouldReturnAllAvatars() {
        val space = spaceCreator.create()
        val url = "https://s3.bazar/avatar"
        val fileId = UUID.randomUUID()

        val saved = spaceAvatarCreator.create(space.id!!, fileId = fileId, url = url)

        val actualResponse = mockMvc.get("/space/${space.id}/avatar") {
            accept = APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
        }.andReturn().response.contentAsString

        val dto = objectMapper.readValue(actualResponse, GetSpaceAvatarsResponse::class.java)

        assertThat(dto.avatars).hasSize(1)
        val avatar = dto.avatars!!.first()
        assertThat(avatar.id).isEqualTo(saved.id)
        assertThat(avatar.status).isEqualTo(SpaceAvatarStatus.SUCCESS.name)
        assertThat(avatar.url).isEqualTo(url)
        assertThat(avatar.createdAt).isNotNull
    }

    @Test
    @DisplayName("Get space avatars returns empty list when no avatars exist")
    fun getSpaceAvatar_shouldReturnEmptyListWhenNoAvatars() {
        val space = spaceCreator.create()

        val actualResponse = mockMvc.get("/space/${space.id}/avatar") {
            accept = APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
        }.andReturn().response.contentAsString

        val dto = objectMapper.readValue(actualResponse, GetSpaceAvatarsResponse::class.java)

        assertThat(dto.avatars).isNotNull
        assertThat(dto.avatars).hasSize(0)
    }

    @Test
    @DisplayName("Initiate avatar upload rejects file larger than max size")
    fun initiateAvatarUpload_shouldRejectTooLargeFile() {
        val space = spaceCreator.create()

        val actualResponse = mockMvc.post("/space/${space.id}/avatar") {
            accept = APPLICATION_JSON
            contentType = APPLICATION_JSON
            content = """{"fileName":"avatar.png","contentType":"image/png","sizeBytes":99999999}"""
        }.andExpect {
            status { isBadRequest() }
        }.andReturn().response.contentAsString

        val error = objectMapper.readValue(actualResponse, ApiErrorResponse::class.java)

        assertThat(error.code).isEqualTo("INVALID_AVATAR_SIZE")
        assertThat(spaceAvatarEntityRepository.findAll()).isEmpty()
    }

    @Test
    @DisplayName("Initiate avatar upload rejects unsupported mime type")
    fun initiateAvatarUpload_shouldRejectUnsupportedMimeType() {
        val space = spaceCreator.create()

        val actualResponse = mockMvc.post("/space/${space.id}/avatar") {
            accept = APPLICATION_JSON
            contentType = APPLICATION_JSON
            content = """{"fileName":"avatar.gif","contentType":"image/gif","sizeBytes":1234}"""
        }.andExpect {
            status { isBadRequest() }
        }.andReturn().response.contentAsString

        val error = objectMapper.readValue(actualResponse, ApiErrorResponse::class.java)

        assertThat(error.code).isEqualTo("INVALID_AVATAR_FILE_TYPE")
        assertThat(spaceAvatarEntityRepository.findAll()).isEmpty()
    }

    @Test
    @DisplayName("Initiate avatar upload rejects unsupported file extension")
    fun initiateAvatarUpload_shouldRejectUnsupportedExtension() {
        val space = spaceCreator.create()

        val actualResponse = mockMvc.post("/space/${space.id}/avatar") {
            accept = APPLICATION_JSON
            contentType = APPLICATION_JSON
            content = """{"fileName":"avatar.gif","contentType":"image/png","sizeBytes":1234}"""
        }.andExpect {
            status { isBadRequest() }
        }.andReturn().response.contentAsString

        val error = objectMapper.readValue(actualResponse, ApiErrorResponse::class.java)

        assertThat(error.code).isEqualTo("INVALID_AVATAR_FILE_EXTENSION")
        assertThat(spaceAvatarEntityRepository.findAll()).isEmpty()
    }
}