package org.bazar.space.api.controller

import org.assertj.core.api.Assertions.assertThat
import org.bazar.space.BaseWebTest
import org.bazar.space.adapter.inbound.rest.error.ApiErrorResponse
import org.bazar.space.adapter.outbound.joinrequest.persistence.SpaceJoinRequestEntityRepository
import org.bazar.space.application.shared.port.out.CurrentUserProvider
import org.bazar.space.domain.exception.NotAuthenticatedException
import org.bazar.space.model.SpaceJoinRequestResponse
import org.bazar.space.utils.SpaceCreator
import org.bazar.space.utils.SpaceJoinRequestCreator
import org.bazar.space.utils.UserSpaceCreator
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.post
import tools.jackson.databind.ObjectMapper
import java.time.Instant
import java.time.temporal.ChronoUnit

class SpaceJoinRequestControllerTest : BaseWebTest() {

    @Autowired
    lateinit var spaceCreator: SpaceCreator

    @Autowired
    lateinit var userSpaceCreator: UserSpaceCreator

    @Autowired
    lateinit var spaceJoinRequestCreator: SpaceJoinRequestCreator

    @Autowired
    lateinit var spaceJoinRequestEntityRepository: SpaceJoinRequestEntityRepository

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockitoBean
    lateinit var currentUserProvider: CurrentUserProvider

    @Test
    @DisplayName("POST /v1/spaces/by-code/{code}/join-requests should create join request for current user")
    fun createJoinRequest_shouldCreateRequest() {
        val space = spaceCreator.create()
        whenever(currentUserProvider.id).thenReturn(authenticatedUserId)
        val before = Instant.now()

        val response = createJoinRequest(space.joinCode)
            .andExpect { status { isOk() } }
            .body<SpaceJoinRequestResponse>()

        val requests = spaceJoinRequestEntityRepository.findAll().toList()
        assertThat(requests).hasSize(1)
        val stored = requests.single()
        assertThat(stored.spaceId).isEqualTo(space.id)
        assertThat(stored.userId).isEqualTo(authenticatedUserId)
        assertThat(response.id).isEqualTo(stored.id)
        assertThat(response.createdAt).isEqualTo(stored.createdAt).isAfterOrEqualTo(before)
        verifyNoInteractions(authorizer)
    }

    @Test
    @DisplayName("POST /v1/spaces/by-code/{code}/join-requests should return existing request when it was already submitted")
    fun createJoinRequest_whenAlreadyRequested_shouldReturnExistingRequest() {
        val space = spaceCreator.create()
        val existing = spaceJoinRequestCreator.create(
            spaceId = space.id!!,
            userId = authenticatedUserId
        )
        whenever(currentUserProvider.id).thenReturn(authenticatedUserId)

        val response = createJoinRequest(space.joinCode)
            .andExpect { status { isOk() } }
            .body<SpaceJoinRequestResponse>()

        val stored = spaceJoinRequestEntityRepository.findAll().single()
        assertThat(stored.id).isEqualTo(existing.id)
        assertThat(response.id).isEqualTo(existing.id)
        assertThat(response.createdAt).isEqualTo(stored.createdAt)
    }

    @Test
    @DisplayName("POST /v1/spaces/by-code/{code}/join-requests twice should be idempotent")
    fun createJoinRequest_twice_shouldReturnSameRequest() {
        val space = spaceCreator.create()
        whenever(currentUserProvider.id).thenReturn(authenticatedUserId)

        val first = createJoinRequest(space.joinCode).andExpect { status { isOk() } }.body<SpaceJoinRequestResponse>()
        val second = createJoinRequest(space.joinCode).andExpect { status { isOk() } }.body<SpaceJoinRequestResponse>()

        assertThat(second).isEqualTo(first)
        assertThat(spaceJoinRequestEntityRepository.findAll()).hasSize(1)
    }

    @Test
    @DisplayName("POST /v1/spaces/by-code/{code}/join-requests should return 409 USER_ALREADY_IN_SPACE for a member")
    fun createJoinRequest_whenAlreadyMember_shouldReturn409() {
        val space = spaceCreator.create()
        userSpaceCreator.create(space.id!!, authenticatedUserId)
        whenever(currentUserProvider.id).thenReturn(authenticatedUserId)

        val error = createJoinRequest(space.joinCode)
            .andExpect { status { isConflict() } }
            .body<ApiErrorResponse>()

        assertThat(error.code).isEqualTo("USER_ALREADY_IN_SPACE")

        assertThat(spaceJoinRequestEntityRepository.findAll()).isEmpty()
    }

    @Test
    @DisplayName("POST /v1/spaces/by-code/{code}/join-requests should return 404 SPACE_NOT_FOUND for unknown code")
    fun createJoinRequest_unknownCode_shouldReturn404() {
        spaceCreator.create()
        whenever(currentUserProvider.id).thenReturn(authenticatedUserId)

        val error = createJoinRequest("unknown-code")
            .andExpect { status { isNotFound() } }
            .body<ApiErrorResponse>()

        assertThat(error.code).isEqualTo("SPACE_NOT_FOUND")

        assertThat(spaceJoinRequestEntityRepository.findAll()).isEmpty()
    }

    @Test
    @DisplayName("POST /v1/spaces/by-code/{code}/join-requests should return 401 for unauthenticated user")
    fun createJoinRequest_unauthenticated_shouldReturn401() {
        val space = spaceCreator.create()
        whenever(currentUserProvider.id).thenThrow(NotAuthenticatedException())

        val error = createJoinRequest(space.joinCode)
            .andExpect { status { isUnauthorized() } }
            .body<ApiErrorResponse>()

        assertThat(error.code).isEqualTo("NOT_AUTHENTICATED")

        assertThat(spaceJoinRequestEntityRepository.findAll()).isEmpty()
    }

    private fun createJoinRequest(code: String): ResultActionsDsl =
        mockMvc.post("/v1/spaces/by-code/$code/join-requests") {
            accept = APPLICATION_JSON
        }

    private inline fun <reified T : Any> ResultActionsDsl.body(): T =
        objectMapper.readValue(andReturn().response.contentAsString, T::class.java)
}
