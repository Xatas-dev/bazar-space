package org.bazar.space.adapter.inbound.rest

import org.bazar.space.api.SpaceJoinRequestControllerApi
import org.bazar.space.application.joinrequest.command.CreateJoinRequestCommand
import org.bazar.space.application.joinrequest.port.`in`.ApproveJoinRequestsUseCase
import org.bazar.space.application.joinrequest.port.`in`.CreateJoinRequestUseCase
import org.bazar.space.application.joinrequest.port.`in`.GetJoinRequestsUseCase
import org.bazar.space.model.ApproveJoinRequestsRequest
import org.bazar.space.model.ApproveJoinRequestsResponse
import org.bazar.space.model.JoinRequestsPageResponse
import org.bazar.space.model.SpaceJoinRequestResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class SpaceJoinRequestController(
    private val createJoinRequestUseCase: CreateJoinRequestUseCase,
    private val approveJoinRequestsUseCase: ApproveJoinRequestsUseCase,
    private val getJoinRequestsUseCase: GetJoinRequestsUseCase,
) : SpaceJoinRequestControllerApi {

    override fun createJoinRequest(@PathVariable code: String): ResponseEntity<SpaceJoinRequestResponse> {
        val request = createJoinRequestUseCase.execute(CreateJoinRequestCommand(code))
        return ResponseEntity.ok(request.toSpaceJoinRequestResponse())
    }

    override fun approveJoinRequests(
        @PathVariable spaceId: Long,
        @RequestBody approveJoinRequestsRequest: ApproveJoinRequestsRequest
    ): ResponseEntity<ApproveJoinRequestsResponse> {
        val approvedCount = approveJoinRequestsUseCase.execute(approveJoinRequestsRequest.toCommand(spaceId))
        return ResponseEntity.ok(ApproveJoinRequestsResponse(approvedCount))
    }

    override fun getJoinRequests(
        @PathVariable spaceId: Long,
        page: Int,
        size: Int
    ): ResponseEntity<JoinRequestsPageResponse> {
        val result = getJoinRequestsUseCase.execute(getJoinRequestsQuery(spaceId, page, size))
        return ResponseEntity.ok(result.toJoinRequestsPageResponse())
    }
}
