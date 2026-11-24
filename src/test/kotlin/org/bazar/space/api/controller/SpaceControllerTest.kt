package org.bazar.space.api.controller

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.awaitility.kotlin.atMost
import org.awaitility.kotlin.await
import org.awaitility.kotlin.untilAsserted
import org.awaitility.kotlin.withPollInterval
import org.bazar.space.BaseWebTest
import org.bazar.space.infrastructure.config.kafka.KafkaProperties
import org.bazar.space.infrastructure.config.kafka.KafkaTestConsumer
import org.bazar.space.adapter.outbound.space.event.SpaceEventDto
import org.bazar.space.adapter.outbound.space.event.SpaceEventType
import org.bazar.space.adapter.outbound.shared.outbox.OutboxEntity
import org.bazar.space.adapter.outbound.space.persistence.SpaceEntity
import org.bazar.space.adapter.outbound.shared.outbox.OutboxStatus
import org.bazar.space.adapter.outbound.space.persistence.SpaceEntityRepository
import org.bazar.space.adapter.outbound.space.persistence.SpaceAvatarEntityRepository
import org.bazar.space.adapter.outbound.userspace.persistence.UserSpaceEntityRepository
import org.bazar.space.utils.SpaceCreator
import org.bazar.space.model.GetSpacesResponse
import org.bazar.space.utils.SpaceAvatarCreator
import org.bazar.space.utils.UserSpaceCreator
import org.bazar.space.utils.repository.JdbcTestHelper
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post
import tools.jackson.databind.ObjectMapper
import java.time.Duration
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class SpaceControllerTest : BaseWebTest() {

    @Autowired
    lateinit var spaceRepository: SpaceEntityRepository

    @Autowired
    lateinit var spaceAvatarEntityRepository: SpaceAvatarEntityRepository

    @Autowired
    lateinit var userRepository: UserSpaceEntityRepository

    @Autowired
    lateinit var jdbcTestHelper: JdbcTestHelper

    @Autowired
    lateinit var spaceCreator: SpaceCreator

    @Autowired
    lateinit var spaceAvatarCreator: SpaceAvatarCreator

    @Autowired
    lateinit var kafkaProperties: KafkaProperties

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var userSpaceCreator: UserSpaceCreator

    private val authenticatedUserId = UUID.fromString("fd71e64e-edb2-4ecb-bc90-7f27c34e0af7")

    @Test
    @DisplayName("Create space with name, make sure it persist, response is 200")
    fun createSpaceWithName_ShouldReturnNewSpace() {
        val spaceName = "DOTA2"
        //when
        mockMvc.post("/space") {
            accept = APPLICATION_JSON
            param("name", spaceName)
        }.andExpect {
            status { is2xxSuccessful() }
            jsonPath("$.name") { value(spaceName) }
        }
        val spaces = spaceRepository.findAll()
        val users = userRepository.findAll()
        assertThat(spaces).hasSize(1)
        val space = spaces.first()
        assertThat(space.name).isEqualTo(spaceName)
        assertThat(space.joinCode).isNotNull.isNotBlank.hasSize(22)
        assertThat(users)
            .hasSize(1)
        val user = users.first()
        assertThat(user.creator).isTrue
    }

    @Test
    @DisplayName("Patch space name, should update and return 200")
    fun patchSpaceName_ShouldReturnOk() {
        val newName = "DOTA2"
        val spaceInDb = spaceCreator.create()
        //when
        mockMvc.patch("/space/${spaceInDb.id}") {
            accept = APPLICATION_JSON
            param("name", newName)
        }.andExpect {
            status { is2xxSuccessful() }
            jsonPath("$.name") { value(newName) }
            jsonPath("$.id") { value(spaceInDb.id!!) }
        }
        //then
        val spaces = spaceRepository.findAll()
        assertThat(spaces)
            .hasSize(1)
            .extracting(SpaceEntity::name)
            .containsExactlyInAnyOrder(tuple(newName))

    }

    @Test
    @DisplayName("GET /space should return space info with active avatar url")
    fun getAllSpaces_shouldReturnAvatarUrl() {
        val spaceId = spaceCreator.create().id!!
        userSpaceCreator.create(spaceId, authenticatedUserId, creator = true)
        val avatarUrl = "https://s3.bazar/space-avatar"
        spaceAvatarCreator.create(spaceId, url = avatarUrl)

        val actualResponse = mockMvc.get("/space") {
            accept = APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
        }.andReturn().response.contentAsString

        val dto = objectMapper.readValue(actualResponse, GetSpacesResponse::class.java)

        assertThat(dto.spaces).hasSize(1)
        assertThat(dto.spaces!!.first().avatarUrl).isEqualTo(avatarUrl)
    }

    @Test
    @DisplayName($$"DELETE /space/${spaceId}, should delete and return OK")
    fun deleteSpace_ShouldDelete() {
        //given
        val spaceInDb = spaceCreator.create()
        val avatarInDb = spaceAvatarCreator.create(spaceInDb.id!!)
        val testConsumer = KafkaTestConsumer(kafkaProperties.producers["space-events"]!!.name, SpaceEventDto::class.java)
        //when
        mockMvc.delete("/space/${spaceInDb.id}") {
            accept = APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
        }
        //then
        val spaces = spaceRepository.findAll()
        assertThat(spaces)
            .hasSize(0)

        await atMost Duration.ofSeconds(30) withPollInterval Duration.ofSeconds(3) untilAsserted {
            testConsumer.poll().first {
                it.key() == spaceInDb.id.toString()
            }.apply {
                assertThat(this.value())
                    .usingRecursiveAssertion()
                    .isEqualTo(SpaceEventDto(type = SpaceEventType.DELETE, spaceId = spaceInDb.id!!))
            }
        }

        val outboxes = jdbcTestHelper.findAll<OutboxEntity>("outbox")
        assertThat(outboxes)
            .hasSize(1)

        outboxes.first().apply {
            assertEquals(entityId, spaceInDb.id)
            assertEquals(status, OutboxStatus.DONE)
            assertTrue {
                updatedAt.isAfter(createdAt)
            }
        }

    }

}
