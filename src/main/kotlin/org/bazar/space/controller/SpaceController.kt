package org.bazar.space.controller

import org.bazar.space.config.authorization.AuthorizationAction.READ_SPACE
import org.bazar.space.config.authorization.SpacePreAuthorize
import org.bazar.space.model.http.request.AddUserToSpaceDtoRequest
import org.bazar.space.service.SpaceManager
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/space")
class SpaceController(
    private val spaceManager: SpaceManager
) {

    @PostMapping("/user")
    fun addUserToSpace(@RequestBody dto: AddUserToSpaceDtoRequest) {
        spaceManager.addUserToSpace(dto.userId, dto.spaceId)
    }

    @DeleteMapping("/{spaceId}/user/{userId}")
    fun deleteUserFromSpace(@PathVariable spaceId: Long, @PathVariable userId: UUID) =
        spaceManager.deleteUserFromSpace(spaceId, userId)

    @GetMapping("/{spaceId}/user")
    @SpacePreAuthorize(READ_SPACE)
    fun getAllUsersInSpace(@PathVariable spaceId: Long) = spaceManager.getAllUsersInSpace(spaceId)


}