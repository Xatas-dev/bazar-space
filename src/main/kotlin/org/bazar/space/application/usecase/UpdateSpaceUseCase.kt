package org.bazar.space.application.usecase

import org.bazar.authorization.sdk.Permission
import org.bazar.space.domain.model.Space
import org.bazar.space.domain.port.Authorizer
import org.bazar.space.domain.port.SpaceRepositoryPort
import org.bazar.space.util.buildAuthorizationRequest
import org.bazar.space.util.exceptions.ApiException
import org.bazar.space.util.exceptions.ApiExceptions
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UpdateSpaceUseCase(
    private val spaceRepositoryPort: SpaceRepositoryPort,
    private val authorizer: Authorizer
) {

    @Transactional
    fun execute(spaceId: Long, name: String, jwtToken: String): Space {
        authorizer.authorizeOrThrow(buildAuthorizationRequest(spaceId, Permission.SPACE_WRITE, jwtToken))
        val space = spaceRepositoryPort.findById(spaceId)
            ?: throw ApiException(ApiExceptions.SPACE_NOT_FOUND, spaceId)
        val updated = space.copy(name = name)
        spaceRepositoryPort.save(updated)
        return updated
    }
}
