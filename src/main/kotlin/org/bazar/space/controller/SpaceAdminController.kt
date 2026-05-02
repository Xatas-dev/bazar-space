package org.bazar.space.controller

import org.bazar.authorization.sdk.Permission
import org.bazar.space.api.SpaceAdminControllerApi
import org.bazar.space.model.AddUserToSpaceDtoRequest
import org.bazar.space.model.GetSpaceDto
import org.bazar.space.service.SpaceAdminApiService
import org.bazar.space.service.authorization.SpaceAuthorizationService
import org.bazar.space.util.buildAuthorizationRequest
import org.bazar.space.util.getAuthenticatedUserIdOrThrow
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class SpaceAdminController(
    private val spaceAdminApiService: SpaceAdminApiService,
    private val spaceAuthorizationService: SpaceAuthorizationService
) : SpaceAdminControllerApi {

    override fun addUserToSpace(addUserToSpaceDtoRequest: AddUserToSpaceDtoRequest): ResponseEntity<Unit> {
        spaceAdminApiService.addUserToSpace(addUserToSpaceDtoRequest.userId, addUserToSpaceDtoRequest.spaceId)
        return ResponseEntity.ok().build()
    }

    override fun createSpace(name: String): ResponseEntity<GetSpaceDto> {
        val response = spaceAdminApiService.createSpace(getAuthenticatedUserIdOrThrow(), name)
        return ResponseEntity.ok(response)
    }

    override fun deleteSpace(@PathVariable spaceId: Long): ResponseEntity<Unit> {
        spaceAdminApiService.deleteSpace(spaceId)
        return ResponseEntity.ok().build()
    }

    override fun deleteUserFromSpace(
        @PathVariable spaceId: Long,
        userId: UUID
    ): ResponseEntity<Unit> {
        spaceAdminApiService.deleteUserFromSpace(spaceId, userId)
        return ResponseEntity.ok().build()
    }

    override fun patchSpace(
        @PathVariable spaceId: Long,
        name: String
    ): ResponseEntity<GetSpaceDto> {
        spaceAuthorizationService.authorizeOrThrow(buildAuthorizationRequest(spaceId, Permission.SPACE_WRITE))
        val response = spaceAdminApiService.updateSpace(spaceId, name)
        return ResponseEntity.ok(response)
    }
}