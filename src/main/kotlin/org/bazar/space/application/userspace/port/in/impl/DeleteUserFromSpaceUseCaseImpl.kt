package org.bazar.space.application.userspace.port.`in`.impl

import org.bazar.space.application.userspace.command.DeleteUserFromSpaceCommand
import org.bazar.space.application.port.`in`.DeleteUserFromSpaceUseCase
import org.bazar.space.domain.auth.Permission
import org.bazar.space.application.shared.port.out.Authorizer
import org.bazar.space.application.shared.port.out.AuthzManager
import org.bazar.space.application.userspace.port.out.UserSpaceRepositoryPort
import org.bazar.space.application.shared.buildAuthorizationCheck
import org.bazar.space.domain.exception.DomainException
import org.bazar.space.domain.exception.DomainErrors
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DeleteUserFromSpaceUseCaseImpl(
    private val userSpaceRepositoryPort: UserSpaceRepositoryPort,
    private val authorizer: Authorizer,
    private val authzManager: AuthzManager
) : DeleteUserFromSpaceUseCase {

    @Transactional
    override fun execute(command: DeleteUserFromSpaceCommand) {
        authorizer.authorizeOrThrow(buildAuthorizationCheck(command.spaceId, Permission.SPACE_USER_DELETE))
        val user = userSpaceRepositoryPort.findBySpaceIdAndUserId(command.spaceId, command.userId)
            ?: throw DomainException(DomainErrors.USER_NOT_FOUND, command.userId, command.spaceId)
        userSpaceRepositoryPort.deleteBySpaceIdAndUserId(command.spaceId, command.userId)
        authzManager.deleteUserInAuthz(command.userId, command.spaceId, user.creator)
    }
}