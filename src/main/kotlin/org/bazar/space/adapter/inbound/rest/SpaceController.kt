package org.bazar.space.adapter.inbound.rest

import org.bazar.space.api.SpaceControllerApi
import org.bazar.space.application.port.`in`.GetSpacesUseCase
import org.bazar.space.application.port.`in`.GetUsersInSpaceUseCase
import org.bazar.space.application.shared.port.out.CurrentUserProvider
import org.bazar.space.application.space.query.GetSpacesQuery
import org.bazar.space.application.userspace.query.GetUsersInSpaceQuery
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
        val spaces = getSpacesUseCase.execute(GetSpacesQuery(currentUser.id))
        return ResponseEntity.ok(spaces.toGetSpacesResponse())
    }

    override fun getAllUsersInSpaceEnriched(@PathVariable spaceId: Long): ResponseEntity<GetUsersInSpaceResponse> {
        val users = getUsersInSpaceUseCase.execute(GetUsersInSpaceQuery(spaceId, currentUser.id))
        return ResponseEntity.ok(users.toGetUsersInSpaceResponse())
    }
}