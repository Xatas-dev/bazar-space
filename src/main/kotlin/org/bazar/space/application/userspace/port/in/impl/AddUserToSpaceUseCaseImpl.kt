package org.bazar.space.application.userspace.port.`in`.impl

import org.bazar.space.application.userspace.command.AddUserToSpaceCommand
import org.bazar.space.application.userspace.port.`in`.AddUserToSpaceUseCase
import org.bazar.space.domain.auth.Permission
import org.bazar.space.application.shared.port.out.Authorizer
import org.bazar.space.application.shared.port.out.AuthzManager
import org.bazar.space.application.userspace.port.out.UserSpaceRepositoryPort
import org.bazar.space.domain.userspace.UserSpace
import org.bazar.space.application.shared.buildAuthorizationCheck
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AddUserToSpaceUseCaseImpl(
    private val userSpaceRepositoryPort: UserSpaceRepositoryPort,
    private val authorizer: Authorizer,
    private val authzManager: AuthzManager
) : AddUserToSpaceUseCase {

    @Transactional
    override fun execute(command: AddUserToSpaceCommand) {
        authorizer.authorizeOrThrow(buildAuthorizationCheck(command.spaceId, Permission.SPACE_USER_ADD))
        userSpaceRepositoryPort.save(UserSpace.createMember(command.spaceId, command.userToAddId))
        authzManager.createUserInAuthz(command.userToAddId, command.spaceId, false)
    }
}