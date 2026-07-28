package org.bazar.space.application.usecase

import org.bazar.authorization.sdk.Permission
import org.bazar.space.domain.port.Authorizer
import org.bazar.space.domain.port.AuthzManager
import org.bazar.space.domain.port.UserSpaceRepositoryPort
import org.bazar.space.domain.model.UserSpace
import org.bazar.space.util.buildAuthorizationRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class AddUserToSpaceUseCase(
    private val userSpaceRepositoryPort: UserSpaceRepositoryPort,
    private val authorizer: Authorizer,
    private val authzManager: AuthzManager
) {

    @Transactional
    fun execute(userToAddId: UUID, spaceId: Long, jwtToken: String) {
        authorizer.authorizeOrThrow(buildAuthorizationRequest(spaceId, Permission.SPACE_USER_ADD, jwtToken))
        userSpaceRepositoryPort.save(UserSpace(spaceId = spaceId, userId = userToAddId, creator = false))
        authzManager.createUserInAuthz(userToAddId, spaceId, false)
    }
}
