package org.bazar.space.application.space.port.`in`.impl

import org.bazar.space.application.space.command.CreateSpaceCommand
import org.bazar.space.application.port.`in`.CreateSpaceUseCase
import org.bazar.space.application.shared.port.out.AuthzManager
import org.bazar.space.application.space.port.out.SpaceRepositoryPort
import org.bazar.space.application.userspace.port.out.UserSpaceRepositoryPort
import org.bazar.space.domain.space.Space
import org.bazar.space.domain.userspace.UserSpace
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateSpaceUseCaseImpl(
    private val spaceRepositoryPort: SpaceRepositoryPort,
    private val userSpaceRepositoryPort: UserSpaceRepositoryPort,
    private val authzManager: AuthzManager
) : CreateSpaceUseCase {

    @Transactional
    override fun execute(command: CreateSpaceCommand): Space {
        val space = spaceRepositoryPort.save(Space.create(command.name))
        val spaceId = requireNotNull(space.id) { "Space id must not be null after save" }
        userSpaceRepositoryPort.save(UserSpace.createOwner(spaceId, command.userId))
        authzManager.createUserInAuthz(command.userId, spaceId, true)
        return space
    }
}