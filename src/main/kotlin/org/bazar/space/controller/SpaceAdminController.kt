package org.bazar.space.controller

import org.bazar.space.api.SpaceAdminControllerApi
import org.bazar.space.model.AddUserToSpaceDtoRequest
import org.bazar.space.model.GetSpaceDto
import org.bazar.space.service.SpaceManager
import org.bazar.space.service.authorization.AuthorizationAction.EDIT_SPACE
import org.bazar.space.service.authorization.SpaceAuthorizationService
import org.bazar.space.util.getAuthenticatedUserIdOrThrow
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class SpaceAdminController(
    private val spaceManager: SpaceManager,
    private val spaceAuthorizationService: SpaceAuthorizationService
) : SpaceAdminControllerApi {

    override fun addUserToSpace(addUserToSpaceDtoRequest: AddUserToSpaceDtoRequest): ResponseEntity<Unit> {
        spaceManager.addUserToSpace(addUserToSpaceDtoRequest.userId, addUserToSpaceDtoRequest.spaceId)
        return ResponseEntity.ok().build()
    }

    override fun createSpace(name: String): ResponseEntity<GetSpaceDto> {
        val response = spaceManager.createSpace(getAuthenticatedUserIdOrThrow(), name)
        return ResponseEntity.ok(response)
    }

    override fun deleteSpace(@PathVariable spaceId: Long): ResponseEntity<Unit> {
        spaceManager.deleteSpace(spaceId)
        return ResponseEntity.ok().build()
    }

    override fun deleteUserFromSpace(
        @PathVariable spaceId: Long,
        userId: UUID
    ): ResponseEntity<Unit> {
        spaceManager.deleteUserFromSpace(spaceId, userId)
        return ResponseEntity.ok().build()
    }

    override fun patchSpace(
        @PathVariable spaceId: Long,
        name: String
    ): ResponseEntity<GetSpaceDto> {
        spaceAuthorizationService.authorizeOrThrow(spaceId, EDIT_SPACE)
        val response = spaceManager.updateSpace(spaceId, name)
        return ResponseEntity.ok(response)
    }
}