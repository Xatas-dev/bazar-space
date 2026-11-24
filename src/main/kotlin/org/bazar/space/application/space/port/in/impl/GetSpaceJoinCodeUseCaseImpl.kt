package org.bazar.space.application.space.port.`in`.impl

import org.bazar.space.application.shared.buildAuthorizationCheck
import org.bazar.space.application.shared.port.out.Authorizer
import org.bazar.space.application.space.port.`in`.GetSpaceJoinCodeUseCase
import org.bazar.space.application.space.port.out.SpaceRepositoryPort
import org.bazar.space.domain.auth.Permission
import org.bazar.space.domain.exception.SpaceNotFoundException
import org.springframework.stereotype.Service

@Service
class GetSpaceJoinCodeUseCaseImpl(
    private val spaceRepositoryPort: SpaceRepositoryPort,
    private val authorizer: Authorizer
) : GetSpaceJoinCodeUseCase {

    override fun execute(spaceId: Long): String {
        authorizer.authorizeOrThrow(buildAuthorizationCheck(spaceId, Permission.SPACE_USER_ADD))
        val space = spaceRepositoryPort.findById(spaceId) ?: throw SpaceNotFoundException(spaceId)
        return space.joinCode
    }
}
