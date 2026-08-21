package org.bazar.space.api.controller

import org.assertj.core.api.Assertions.assertThat
import org.bazar.space.BaseWebTest
import org.bazar.space.model.GetUsersInSpaceResponse
import org.bazar.space.model.SimpleRoleDto
import org.bazar.space.model.UserInSpaceDto
import org.bazar.space.application.userspace.port.out.GetRoleNameDto
import org.bazar.space.application.userspace.port.out.UserPersonaDto
import org.bazar.space.adapter.outbound.client.GetRoleNamesResponse
import org.bazar.space.adapter.outbound.client.BazarAuthorizationFeignClient
import org.bazar.space.adapter.outbound.client.BazarPersonaFeignClient
import org.bazar.space.model.UserInSpaceRawDto
import org.bazar.space.utils.InitDataHelper
import org.junit.jupiter.api.DisplayName
import org.mockito.Mockito.`when` as whenever
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyList
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.get
import tools.jackson.databind.ObjectMapper
import java.util.UUID


class SpaceControllerTest: BaseWebTest() {

    @MockitoBean
    private lateinit var bazarPersonaFeignClient: BazarPersonaFeignClient

    @MockitoBean
    private lateinit var bazarAuthorizationFeignClient: BazarAuthorizationFeignClient

    @Autowired
    private lateinit var initDataHelper: InitDataHelper

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    @DisplayName("Trigger GET /spaces/{spaceId}/users twice, should return expected response and call external API only once")
    fun getAllUsersEnriched_shouldReturnOk() {
        //given
        val spaceId = initDataHelper.createSpace()
        val randomUsers = List(5) {
            initDataHelper.createUser(spaceId)
        }
        val authenticatedUser = initDataHelper.createUser(spaceId, UUID.fromString("fd71e64e-edb2-4ecb-bc90-7f27c34e0af7"), creator = true)
        val users = randomUsers + authenticatedUser

        val mockPersonaResponse = List(6) { index ->
            UserPersonaDto(
                users[index].userId,
                "test$index",
                "email$index",
                "TestFirstName$index",
                "TestLastName$index"
            )
        }

        val mockAuthorizationResponse = GetRoleNamesResponse(
            List(6) { index ->
                GetRoleNameDto(
                    index.toLong(),
                    "CustomRole$index",
                    users[index].userId.toString(),
                    true
                )
            }
        )

        whenever(bazarPersonaFeignClient.getUsersByIds(anyList())).thenReturn(mockPersonaResponse)
        whenever(bazarAuthorizationFeignClient.getRoleNames(anyLong(), anyList())).thenReturn(mockAuthorizationResponse)

        //when
        mockMvc.get("/spaces/$spaceId/users")

        val actualResponse = mockMvc.get("/spaces/$spaceId/users")
            .andExpect {
                status { isOk() }
            }.andReturn().response.contentAsString
        val actualResponseDto = objectMapper.readValue(actualResponse, GetUsersInSpaceResponse::class.java)

        //then

        assertThat(actualResponseDto).isNotNull

        assertThat(actualResponseDto.users)
            .isNotNull
            .hasSize(6)
            .containsExactlyInAnyOrderElementsOf(
                users.mapIndexed { index, user ->
                    UserInSpaceDto(
                        user.userId,
                        spaceId,
                        mockPersonaResponse[index].userName,
                        mockPersonaResponse[index].firstName,
                        mockPersonaResponse[index].lastName,
                        user.creator,
                        SimpleRoleDto(
                            mockAuthorizationResponse.roles[index].id,
                            mockAuthorizationResponse.roles[index].name,
                            mockAuthorizationResponse.roles[index].isVisible
                        )

                    )
                }
            )
        verify(bazarAuthorizationFeignClient, times(1)).getRoleNames(anyLong(), anyList())
        verify(bazarPersonaFeignClient, times(1)).getUsersByIds(anyList())
    }

    @Test
    @DisplayName("Trigger GET /spaces/{spaceId}/users, should return 403 due to lack of permission")
    fun getAllUsersEnriched_shouldReturn403() {
        //given
        val spaceId = initDataHelper.createSpace()
        repeat(5) {
            initDataHelper.createUser(spaceId)
        }

        //when and then
        mockMvc.get("/spaces/$spaceId/users")
            .andExpect {
                status { isForbidden() }
            }

    }

    @Test
    @DisplayName("Trigger GET /spaces/{spaceId}/users/{userId}/raw, should return enriched user and call external API only once")
    fun getUserInSpace_shouldReturnOk() {
        //given
        val spaceId = initDataHelper.createSpace()
        initDataHelper.createUser(spaceId, UUID.fromString("fd71e64e-edb2-4ecb-bc90-7f27c34e0af7"))
        val requestedUser = initDataHelper.createUser(spaceId, UUID.randomUUID(), true)

        //when
        val actualResponse = mockMvc.get("/spaces/$spaceId/users/${requestedUser.userId}/raw")
            .andExpect {
                status { isOk() }
            }.andReturn().response.contentAsString
        val actualResponseDto = objectMapper.readValue(actualResponse, UserInSpaceRawDto::class.java)

        //then
        assertThat(actualResponseDto).isEqualTo(
            UserInSpaceRawDto(
                requestedUser.userId,
                spaceId,
                requestedUser.creator
            )
        )
    }

    @Test
    @DisplayName("Trigger GET /spaces/{spaceId}/users/{userId}/raw for authenticated user himself, should return enriched user")
    fun getUserInSpace_shouldReturnOk_whenAuthenticatedUserRequestsHimself() {
        //given
        val spaceId = initDataHelper.createSpace()
        val authenticatedUserId = UUID.fromString("fd71e64e-edb2-4ecb-bc90-7f27c34e0af7")
        initDataHelper.createUser(spaceId, authenticatedUserId, creator = true)

        //when
        val actualResponse = mockMvc.get("/spaces/$spaceId/users/$authenticatedUserId/raw")
            .andExpect {
                status { isOk() }
            }.andReturn().response.contentAsString
        val actualResponseDto = objectMapper.readValue(actualResponse, UserInSpaceRawDto::class.java)

        //then
        assertThat(actualResponseDto).isEqualTo(
            UserInSpaceRawDto(
                authenticatedUserId,
                spaceId,
                true
            )
        )
    }

    @Test
    @DisplayName("Trigger GET /spaces/{spaceId}/users/{userId}/raw, should return 404 when user is not member of the space")
    fun getUserInSpace_shouldReturn404() {
        //given
        val spaceId = initDataHelper.createSpace()
        initDataHelper.createUser(spaceId, UUID.fromString("fd71e64e-edb2-4ecb-bc90-7f27c34e0af7"))

        //when and then
        mockMvc.get("/spaces/$spaceId/users/${UUID.randomUUID()}/raw")
            .andExpect {
                status { isNotFound() }
            }
    }

}
