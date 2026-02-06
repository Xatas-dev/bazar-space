package org.bazar.space.controller

import org.bazar.space.api.SpaceControllerApi
import org.bazar.space.model.GetSpacesResponse
import org.bazar.space.service.SpaceManager
import org.bazar.space.service.authorization.AuthorizationAction
import org.bazar.space.service.authorization.AuthorizationAction.READ_SPACE
import org.bazar.space.service.authorization.SpaceAuthorizationService
import org.bazar.space.util.getAuthenticatedUserIdOrThrow
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class SpaceController(
    private val spaceManager: SpaceManager,
    private val spaceAuthorizationService: SpaceAuthorizationService
) : SpaceControllerApi {

    override fun getAllSpaces(): ResponseEntity<GetSpacesResponse> {
        val response = spaceManager.getAllSpacesByUserId(getAuthenticatedUserIdOrThrow())
        return ResponseEntity.ok(response)
    }

    override fun getAllUsersInSpace(@PathVariable spaceId: Long): ResponseEntity<List<UUID>> {
        spaceAuthorizationService.authorizeOrThrow(spaceId, READ_SPACE)
        val response = spaceManager.getAllUsersInSpace(spaceId)
        return ResponseEntity.ok(response)
    }


}