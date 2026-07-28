package org.bazar.space.api.controller

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.awaitility.kotlin.atMost
import org.awaitility.kotlin.await
import org.awaitility.kotlin.untilAsserted
import org.awaitility.kotlin.withPollInterval
import org.bazar.space.BaseWebTest
import org.bazar.space.config.kafka.KafkaProperties
import org.bazar.space.config.kafka.KafkaTestConsumer
import org.bazar.space.infrastructure.event.SpaceEventDto
import org.bazar.space.infrastructure.event.SpaceEventType
import org.bazar.space.infrastructure.persistence.entity.Outbox
import org.bazar.space.infrastructure.persistence.entity.SpaceEntity
import org.bazar.space.infrastructure.persistence.entity.OutboxStatus
import org.bazar.space.infrastructure.persistence.repository.SpaceEntityRepository
import org.bazar.space.infrastructure.persistence.repository.UserSpaceEntityRepository
import org.bazar.space.utils.SpaceCreator
import org.bazar.space.utils.repository.JdbcTestHelper
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.doReturn
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post
import java.time.Duration
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class SpaceAdminControllerTest : BaseWebTest() {

    @Autowired
    lateinit var spaceRepository: SpaceEntityRepository

    @Autowired
    lateinit var userRepository: UserSpaceEntityRepository

    @Autowired
    lateinit var jdbcTestHelper: JdbcTestHelper

    @Autowired
    lateinit var spaceCreator: SpaceCreator

    @Autowired
    lateinit var kafkaProperties: KafkaProperties

    @Test
    @DisplayName("Create space with name, make sure it persist, response is 200")
    fun createSpaceWithName_ShouldReturnNewSpace() {
        //given
        val spaceName = "DOTA2"
        doReturn(true).`when`(bazarAuthorizationClient).authorize(any())
        //when
        mockMvc.post("/space") {
            accept = APPLICATION_JSON
            param("name", spaceName)
        }.andExpect {
            status { is2xxSuccessful() }
            jsonPath("$.name") { value(spaceName) }
        }
        //then
        val spaces = spaceRepository.findAll()
        val users = userRepository.findAll()
        assertThat(spaces)
            .hasSize(1)
            .extracting(SpaceEntity::name)
            .containsExactlyInAnyOrder(tuple(spaceName))
        assertThat(users)
            .hasSize(1)
        val user = users.first()
        assertThat(user.creator).isTrue
    }

    @Test
    @DisplayName("Patch space name, should update and return 200")
    fun patchSpaceName_ShouldReturnOk() {
        //given
        val newName = "DOTA2"
        val spaceInDb = spaceCreator.create()
        doReturn(true).`when`(bazarAuthorizationClient).authorize(any())
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
    @DisplayName($$"DELETE /space/${spaceId}, should delete and return OK")
    fun deleteSpace_ShouldDelete() {
        //given
        val spaceInDb = spaceCreator.create()
        val testConsumer = KafkaTestConsumer(kafkaProperties.producers["space-events"]!!.name, SpaceEventDto::class.java)
        doReturn(true).`when`(bazarAuthorizationClient).authorize(any())
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

        val outboxes = jdbcTestHelper.findAll<Outbox>("outbox")
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