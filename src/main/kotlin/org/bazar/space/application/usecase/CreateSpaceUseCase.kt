package org.bazar.space.application.usecase

import org.bazar.space.domain.port.AuthzManager
import org.bazar.space.domain.port.SpaceRepositoryPort
import org.bazar.space.domain.port.UserSpaceRepositoryPort
import org.bazar.space.domain.model.Space
import org.bazar.space.domain.model.UserSpace
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class CreateSpaceUseCase(
    private val spaceRepositoryPort: SpaceRepositoryPort,
    private val userSpaceRepositoryPort: UserSpaceRepositoryPort,
    private val authzManager: AuthzManager
) {

    @Transactional
    fun execute(userId: UUID, name: String): Space {
        val space = spaceRepositoryPort.save(Space(name = name))
        userSpaceRepositoryPort.save(UserSpace(spaceId = space.id!!, userId = userId, creator = true))
        authzManager.createUserInAuthz(userId, space.id!!, true)
        return space
    }
}
