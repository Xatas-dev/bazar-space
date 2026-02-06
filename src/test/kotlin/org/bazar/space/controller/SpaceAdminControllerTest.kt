package org.bazar.space.controller

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.bazar.space.BaseWebTest
import org.bazar.space.persistence.entity.Space
import org.bazar.space.persistence.repository.SpaceRepository
import org.bazar.space.persistence.repository.UserSpaceRepository
import org.bazar.space.utils.SpaceCreator
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post


class SpaceAdminControllerTest : BaseWebTest() {

    @Autowired
    lateinit var spaceRepository: SpaceRepository

    @Autowired
    lateinit var userRepository: UserSpaceRepository

    @Autowired
    lateinit var spaceCreator: SpaceCreator

    @Test
    @DisplayName("Create space with name, make sure it persist, response is 200")
    fun createSpaceWithName_ShouldReturnNewSpace() {
        //given
        val spaceName = "DOTA2"
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
        assertThat(spaces)
            .hasSize(1)
            .extracting(Space::name)
            .containsExactlyInAnyOrder(tuple(spaceName))

    }

    @Test
    @DisplayName("Patch space name, should update and return 200")
    fun patchSpaceName_ShouldReturnOk() {
        //given
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
            .extracting(Space::name)
            .containsExactlyInAnyOrder(tuple(newName))

    }

}