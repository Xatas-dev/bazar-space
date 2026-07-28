package org.bazar.space.api.controller

import org.bazar.space.api.SpaceAdminControllerApi
import org.bazar.space.application.usecase.AddUserToSpaceUseCase
import org.bazar.space.application.usecase.CreateSpaceUseCase
import org.bazar.space.application.usecase.DeleteSpaceUseCase
import org.bazar.space.application.usecase.DeleteUserFromSpaceUseCase
import org.bazar.space.application.usecase.UpdateSpaceUseCase
import org.bazar.space.domain.port.CurrentUserProvider
import org.bazar.space.model.AddUserToSpaceDtoRequest
import org.bazar.space.model.GetSpaceDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class SpaceAdminController(
    private val createSpaceUseCase: CreateSpaceUseCase,
    private val deleteSpaceUseCase: DeleteSpaceUseCase,
    private val updateSpaceUseCase: UpdateSpaceUseCase,
    private val addUserToSpaceUseCase: AddUserToSpaceUseCase,
    private val deleteUserFromSpaceUseCase: DeleteUserFromSpaceUseCase,
    private val currentUser: CurrentUserProvider,
) : SpaceAdminControllerApi {

    override fun addUserToSpace(addUserToSpaceDtoRequest: AddUserToSpaceDtoRequest): ResponseEntity<Unit> {
        addUserToSpaceUseCase.execute(addUserToSpaceDtoRequest.userId, addUserToSpaceDtoRequest.spaceId, currentUser.jwtToken)
        return ResponseEntity.ok().build()
    }

    override fun createSpace(name: String): ResponseEntity<GetSpaceDto> {
        val space = createSpaceUseCase.execute(currentUser.id, name)
        return ResponseEntity.ok(space.toGetSpaceDto())
    }

    override fun deleteSpace(@PathVariable spaceId: Long): ResponseEntity<Unit> {
        deleteSpaceUseCase.execute(spaceId, currentUser.jwtToken)
        return ResponseEntity.ok().build()
    }

    override fun deleteUserFromSpace(
        @PathVariable spaceId: Long,
        userId: UUID
    ): ResponseEntity<Unit> {
        deleteUserFromSpaceUseCase.execute(spaceId, userId, currentUser.jwtToken)
        return ResponseEntity.ok().build()
    }

    override fun patchSpace(
        @PathVariable spaceId: Long,
        name: String
    ): ResponseEntity<GetSpaceDto> {
        val space = updateSpaceUseCase.execute(spaceId, name, currentUser.jwtToken)
        return ResponseEntity.ok(space.toGetSpaceDto())
    }
}
