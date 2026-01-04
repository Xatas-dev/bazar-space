package org.bazar.space.controller

import org.bazar.space.config.authorization.AuthorizationAction
import org.bazar.space.config.authorization.SpacePreAuthorize
import org.bazar.space.model.http.response.GetUserSpacesDtoResponse
import org.bazar.space.service.SpaceManager
import org.bazar.space.util.extension.toUuid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/space")
class SpaceAdminController(
    final val spaceManager: SpaceManager
) {

    @PostMapping
    fun createSpace(@AuthenticationPrincipal user: Jwt) = spaceManager.createSpace(user.subject.toUuid())


    @GetMapping
    fun getAllUserSpaces(@AuthenticationPrincipal user: Jwt) =
        spaceManager.getAllUserSpaces(user.subject.toUuid())

    @DeleteMapping("/{spaceId}")
    fun deleteSpace(@PathVariable spaceId: Long) {
        spaceManager.deleteSpace(spaceId)
    }

}