package org.bazar.space.adapter.inbound.rest

import org.bazar.space.api.SpaceMemberControllerApi
import org.bazar.space.application.userspace.port.`in`.GetRawUserInSpaceUseCase
import org.bazar.space.application.port.`in`.GetUsersInSpaceUseCase
import org.bazar.space.application.shared.port.out.CurrentUserProvider
import org.bazar.space.application.userspace.command.AddUserToSpaceCommand
import org.bazar.space.application.userspace.command.DeleteUserFromSpaceCommand
import org.bazar.space.application.userspace.port.`in`.AddUserToSpaceUseCase
import org.bazar.space.application.port.`in`.DeleteUserFromSpaceUseCase
import org.bazar.space.application.userspace.query.GetUserInSpaceQuery
import org.bazar.space.application.userspace.query.GetUsersInSpaceQuery
import org.bazar.space.model.AddUserToSpaceDtoRequest
import org.bazar.space.model.GetUsersInSpaceResponse
import org.bazar.space.model.UserInSpaceRawDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class SpaceMemberController(
    private val getUsersInSpaceUseCase: GetUsersInSpaceUseCase,
    private val getRawUserInSpaceUseCase: GetRawUserInSpaceUseCase,
    private val addUserToSpaceUseCase: AddUserToSpaceUseCase,
    private val deleteUserFromSpaceUseCase: DeleteUserFromSpaceUseCase,
    private val currentUser: CurrentUserProvider,
) : SpaceMemberControllerApi {

    override fun getAllUsersInSpaceEnriched(@PathVariable spaceId: Long): ResponseEntity<GetUsersInSpaceResponse> {
        val users = getUsersInSpaceUseCase.execute(GetUsersInSpaceQuery(spaceId, currentUser.id))
        return ResponseEntity.ok(users.toGetUsersInSpaceResponse())
    }

    override fun getUserInSpace(@PathVariable spaceId: Long, @PathVariable userId: UUID): ResponseEntity<UserInSpaceRawDto> {
        val user = getRawUserInSpaceUseCase.execute(GetUserInSpaceQuery(spaceId, userId, currentUser.id))
        return ResponseEntity.ok(user.toUserInSpaceRawDto())
    }

    override fun addUserToSpace(addUserToSpaceDtoRequest: AddUserToSpaceDtoRequest): ResponseEntity<Unit> {
        addUserToSpaceUseCase.execute(
            AddUserToSpaceCommand(addUserToSpaceDtoRequest.userId, addUserToSpaceDtoRequest.spaceId)
        )
        return ResponseEntity.ok().build()
    }

    override fun deleteUserFromSpace(
        @PathVariable spaceId: Long,
        userId: UUID
    ): ResponseEntity<Unit> {
        deleteUserFromSpaceUseCase.execute(DeleteUserFromSpaceCommand(spaceId, userId))
        return ResponseEntity.ok().build()
    }
}