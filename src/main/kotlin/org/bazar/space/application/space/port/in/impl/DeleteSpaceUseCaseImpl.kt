package org.bazar.space.application.space.port.`in`.impl

import org.bazar.space.application.space.command.DeleteSpaceCommand
import org.bazar.space.application.port.`in`.DeleteSpaceUseCase
import org.bazar.space.domain.auth.Permission
import org.bazar.space.application.shared.port.out.Authorizer
import org.bazar.space.application.shared.port.out.AuthzManager
import org.bazar.space.application.shared.port.out.EventPublisher
import org.bazar.space.application.space.port.out.SpaceRepositoryPort
import org.bazar.space.application.userspace.port.out.UserSpaceRepositoryPort
import org.bazar.space.domain.space.SpaceDomainEvent
import org.bazar.space.application.shared.buildAuthorizationCheck
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DeleteSpaceUseCaseImpl(
    private val spaceRepositoryPort: SpaceRepositoryPort,
    private val userSpaceRepositoryPort: UserSpaceRepositoryPort,
    private val authorizer: Authorizer,
    private val authzManager: AuthzManager,
    private val eventPublisher: EventPublisher
) : DeleteSpaceUseCase {

    @Transactional
    override fun execute(command: DeleteSpaceCommand) {
        authorizer.authorizeOrThrow(buildAuthorizationCheck(command.spaceId, Permission.SPACE_DELETE))
        userSpaceRepositoryPort.deleteAllBySpaceId(command.spaceId)
        spaceRepositoryPort.deleteById(command.spaceId)
        eventPublisher.publish(SpaceDomainEvent.Deleted(command.spaceId))
        authzManager.deleteSpaceInAuthz(command.spaceId)
    }
}