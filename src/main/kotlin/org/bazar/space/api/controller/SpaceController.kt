package org.bazar.space.api.controller

import org.bazar.space.api.SpaceControllerApi
import org.bazar.space.application.usecase.GetSpacesUseCase
import org.bazar.space.application.usecase.GetUsersInSpaceUseCase
import org.bazar.space.domain.port.CurrentUserProvider
import org.bazar.space.model.GetSpacesResponse
import org.bazar.space.model.GetUsersInSpaceResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class SpaceController(
    private val getSpacesUseCase: GetSpacesUseCase,
    private val getUsersInSpaceUseCase: GetUsersInSpaceUseCase,
    private val currentUser: CurrentUserProvider,
) : SpaceControllerApi {

    override fun getAllSpaces(): ResponseEntity<GetSpacesResponse> {
        val spaces = getSpacesUseCase.execute(currentUser.id)
        return ResponseEntity.ok(spaces.toGetSpacesResponse())
    }

    override fun getAllUsersInSpaceEnriched(@PathVariable spaceId: Long): ResponseEntity<GetUsersInSpaceResponse> {
        val users = getUsersInSpaceUseCase.execute(spaceId, currentUser.id)
        return ResponseEntity.ok(users.toGetUsersInSpaceResponse())
    }
}
