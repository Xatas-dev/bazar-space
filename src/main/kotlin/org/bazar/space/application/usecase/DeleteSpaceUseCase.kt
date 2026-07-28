package org.bazar.space.application.usecase

import org.bazar.space.domain.port.Authorizer
import org.bazar.space.domain.port.AuthzManager
import org.bazar.space.domain.port.EventPublisher
import org.bazar.space.domain.port.SpaceRepositoryPort
import org.bazar.space.domain.port.UserSpaceRepositoryPort
import org.bazar.space.domain.event.SpaceDomainEvent
import org.bazar.authorization.sdk.Permission
import org.bazar.space.util.buildAuthorizationRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DeleteSpaceUseCase(
    private val spaceRepositoryPort: SpaceRepositoryPort,
    private val userSpaceRepositoryPort: UserSpaceRepositoryPort,
    private val authorizer: Authorizer,
    private val authzManager: AuthzManager,
    private val eventPublisher: EventPublisher
) {

    @Transactional
    fun execute(spaceId: Long, jwtToken: String) {
        authorizer.authorizeOrThrow(buildAuthorizationRequest(spaceId, Permission.SPACE_DELETE, jwtToken))
        userSpaceRepositoryPort.deleteAllBySpaceId(spaceId)
        spaceRepositoryPort.deleteById(spaceId)
        eventPublisher.publish(SpaceDomainEvent.Deleted(spaceId))
        authzManager.deleteSpaceInAuthz(spaceId)
    }
}
