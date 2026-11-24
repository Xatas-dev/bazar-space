package org.bazar.space.application.joinrequest.port.`in`.impl

import org.bazar.space.application.joinrequest.command.CreateJoinRequestCommand
import org.bazar.space.application.joinrequest.port.`in`.CreateJoinRequestUseCase
import org.bazar.space.application.joinrequest.port.out.SpaceJoinRequestRepositoryPort
import org.bazar.space.application.space.port.out.SpaceRepositoryPort
import org.bazar.space.application.userspace.port.out.UserSpaceRepositoryPort
import org.bazar.space.domain.exception.SpaceNotFoundException
import org.bazar.space.domain.exception.UserAlreadyInSpaceException
import org.bazar.space.domain.joinrequest.SpaceJoinRequest
import org.springframework.stereotype.Service

@Service
class CreateJoinRequestUseCaseImpl(
    private val spaceRepositoryPort: SpaceRepositoryPort,
    private val userSpaceRepositoryPort: UserSpaceRepositoryPort,
    private val spaceJoinRequestRepositoryPort: SpaceJoinRequestRepositoryPort
) : CreateJoinRequestUseCase {

    override fun execute(command: CreateJoinRequestCommand): SpaceJoinRequest {
        val space =
            spaceRepositoryPort.findByJoinCode(command.joinCode) ?: throw SpaceNotFoundException(command.joinCode)
        val spaceId = requireNotNull(space.id) { "Persisted space must have id" }

        if (userSpaceRepositoryPort.findBySpaceIdAndUserId(spaceId, command.userId) != null) {
            throw UserAlreadyInSpaceException(command.userId, spaceId)
        }

        return spaceJoinRequestRepositoryPort.save(SpaceJoinRequest.create(spaceId, command.userId))
    }
}
