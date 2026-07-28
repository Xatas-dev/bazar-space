package org.bazar.space.application.usecase

import org.bazar.authorization.sdk.Permission
import org.bazar.space.domain.port.Authorizer
import org.bazar.space.domain.port.AuthzManager
import org.bazar.space.domain.port.UserSpaceRepositoryPort
import org.bazar.space.util.buildAuthorizationRequest
import org.bazar.space.util.exceptions.ApiException
import org.bazar.space.util.exceptions.ApiExceptions
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class DeleteUserFromSpaceUseCase(
    private val userSpaceRepositoryPort: UserSpaceRepositoryPort,
    private val authorizer: Authorizer,
    private val authzManager: AuthzManager
) {

    @Transactional
    fun execute(spaceId: Long, userId: UUID, jwtToken: String) {
        authorizer.authorizeOrThrow(buildAuthorizationRequest(spaceId, Permission.SPACE_USER_DELETE, jwtToken))
        val user = userSpaceRepositoryPort.findBySpaceIdAndUserId(spaceId, userId)
            ?: throw ApiException(ApiExceptions.USER_NOT_FOUND, userId, spaceId)
        userSpaceRepositoryPort.deleteBySpaceIdAndUserId(spaceId, userId)
        authzManager.deleteUserInAuthz(userId, spaceId, user.creator)
    }
}
