package org.bazar.space.adapter.inbound.rest

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.bazar.space.application.joinrequest.command.ApproveJoinRequestsCommand
import org.bazar.space.domain.exception.InvalidRequestException
import org.bazar.space.model.ApproveJoinRequestsRequest
import org.junit.jupiter.api.Test

class SpaceJoinRequestDtoMapperTest {

    private val spaceId = 1L

    @Test
    fun `toCommand maps requestIds to Selected command without duplicates`() {
        val command = ApproveJoinRequestsRequest(requestIds = listOf(1, 2, 2)).toCommand(spaceId)

        assertThat(command).isEqualTo(ApproveJoinRequestsCommand.Selected(spaceId, setOf(1, 2)))
    }

    @Test
    fun `toCommand maps all = true to All command`() {
        val command = ApproveJoinRequestsRequest(all = true).toCommand(spaceId)

        assertThat(command).isEqualTo(ApproveJoinRequestsCommand.All(spaceId))
    }

    @Test
    fun `toCommand rejects invalid requests with details`() {
        val expectedDetailsByRequest = mapOf(
            ApproveJoinRequestsRequest() to "one of requestIds / all must be passed, got none",
            ApproveJoinRequestsRequest(requestIds = listOf(1), all = true) to
                "only one of requestIds / all must be passed, got both",
            ApproveJoinRequestsRequest(all = false) to "all can only be true, got false",
        )

        expectedDetailsByRequest.forEach { (request, expectedDetails) ->
            assertThatThrownBy { request.toCommand(spaceId) }
                .describedAs(request.toString())
                .isInstanceOfSatisfying(InvalidRequestException::class.java) {
                    assertThat(it.details).isEqualTo(expectedDetails)
                }
        }
    }
}
