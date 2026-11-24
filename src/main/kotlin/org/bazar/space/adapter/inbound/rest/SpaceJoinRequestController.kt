package org.bazar.space.adapter.inbound.rest

import org.bazar.space.api.SpaceJoinRequestControllerApi
import org.bazar.space.application.joinrequest.command.CreateJoinRequestCommand
import org.bazar.space.application.joinrequest.port.`in`.CreateJoinRequestUseCase
import org.bazar.space.application.shared.port.out.CurrentUserProvider
import org.bazar.space.model.SpaceJoinRequestResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class SpaceJoinRequestController(
    private val createJoinRequestUseCase: CreateJoinRequestUseCase,
    private val currentUser: CurrentUserProvider,
) : SpaceJoinRequestControllerApi {

    override fun createJoinRequest(@PathVariable code: String): ResponseEntity<SpaceJoinRequestResponse> {
        val request = createJoinRequestUseCase.execute(CreateJoinRequestCommand(code, currentUser.id))
        return ResponseEntity.ok(request.toSpaceJoinRequestResponse())
    }
}
