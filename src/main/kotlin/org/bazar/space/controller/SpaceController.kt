package org.bazar.space.controller

import org.bazar.space.api.SpaceControllerApi
import org.bazar.space.model.GetSpacesResponse
import org.bazar.space.model.GetUsersInSpaceResponse
import org.bazar.space.service.SpaceApiService
import org.bazar.space.util.getAuthenticatedUserIdOrThrow
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class SpaceController(
    private val spaceApiService: SpaceApiService,
) : SpaceControllerApi {

    override fun getAllSpaces(): ResponseEntity<GetSpacesResponse> {
        val response = spaceApiService.getAllSpacesByUserId(getAuthenticatedUserIdOrThrow())
        return ResponseEntity.ok(response)
    }

    override fun getAllUsersInSpaceEnriched(@PathVariable spaceId: Long): ResponseEntity<GetUsersInSpaceResponse> {
        val response = spaceApiService.getAllUsersInSpaceEnriched(spaceId)
        return ResponseEntity.ok(response)
    }

}