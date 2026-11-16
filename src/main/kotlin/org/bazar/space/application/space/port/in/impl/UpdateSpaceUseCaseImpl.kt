package org.bazar.space.application.space.port.`in`.impl

import org.bazar.space.application.space.command.UpdateSpaceCommand
import org.bazar.space.application.port.`in`.UpdateSpaceUseCase
import org.bazar.space.domain.auth.Permission
import org.bazar.space.domain.space.Space
import org.bazar.space.application.shared.port.out.Authorizer
import org.bazar.space.application.space.port.out.SpaceRepositoryPort
import org.bazar.space.application.shared.buildAuthorizationCheck
import org.bazar.space.domain.exception.SpaceNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UpdateSpaceUseCaseImpl(
    private val spaceRepositoryPort: SpaceRepositoryPort,
    private val authorizer: Authorizer
) : UpdateSpaceUseCase {

    @Transactional
    override fun execute(command: UpdateSpaceCommand): Space {
        authorizer.authorizeOrThrow(buildAuthorizationCheck(command.spaceId, Permission.SPACE_WRITE))
        val space = spaceRepositoryPort.findById(command.spaceId)
            ?: throw SpaceNotFoundException(command.spaceId)
        val updated = space.rename(command.name)
        spaceRepositoryPort.save(updated)
        return updated
    }
}