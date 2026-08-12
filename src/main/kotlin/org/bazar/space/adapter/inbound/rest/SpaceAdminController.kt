package org.bazar.space.adapter.inbound.rest

import org.bazar.space.api.SpaceAdminControllerApi
import org.bazar.space.application.userspace.command.AddUserToSpaceCommand
import org.bazar.space.application.space.command.CreateSpaceCommand
import org.bazar.space.application.space.command.DeleteSpaceCommand
import org.bazar.space.application.userspace.command.DeleteUserFromSpaceCommand
import org.bazar.space.application.space.command.UpdateSpaceCommand
import org.bazar.space.application.userspace.port.`in`.AddUserToSpaceUseCase
import org.bazar.space.application.port.`in`.CreateSpaceUseCase
import org.bazar.space.application.port.`in`.DeleteSpaceUseCase
import org.bazar.space.application.port.`in`.DeleteUserFromSpaceUseCase
import org.bazar.space.application.port.`in`.UpdateSpaceUseCase
import org.bazar.space.application.shared.port.out.CurrentUserProvider
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
        addUserToSpaceUseCase.execute(AddUserToSpaceCommand(addUserToSpaceDtoRequest.userId, addUserToSpaceDtoRequest.spaceId))
        return ResponseEntity.ok().build()
    }

    override fun createSpace(name: String): ResponseEntity<GetSpaceDto> {
        val space = createSpaceUseCase.execute(CreateSpaceCommand(currentUser.id, name))
        return ResponseEntity.ok(space.toGetSpaceDto())
    }

    override fun deleteSpace(@PathVariable spaceId: Long): ResponseEntity<Unit> {
        deleteSpaceUseCase.execute(DeleteSpaceCommand(spaceId))
        return ResponseEntity.ok().build()
    }

    override fun deleteUserFromSpace(
        @PathVariable spaceId: Long,
        userId: UUID
    ): ResponseEntity<Unit> {
        deleteUserFromSpaceUseCase.execute(DeleteUserFromSpaceCommand(spaceId, userId))
        return ResponseEntity.ok().build()
    }

    override fun patchSpace(
        @PathVariable spaceId: Long,
        name: String
    ): ResponseEntity<GetSpaceDto> {
        val space = updateSpaceUseCase.execute(UpdateSpaceCommand(spaceId, name))
        return ResponseEntity.ok(space.toGetSpaceDto())
    }
}