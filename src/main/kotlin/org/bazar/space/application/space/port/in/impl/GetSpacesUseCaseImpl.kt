package org.bazar.space.application.space.port.`in`.impl

import org.bazar.space.application.port.`in`.GetSpacesUseCase
import org.bazar.space.application.shared.port.out.CurrentUserProvider
import org.bazar.space.domain.space.Space
import org.bazar.space.application.space.port.out.SpaceRepositoryPort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetSpacesUseCaseImpl(
    private val spaceRepositoryPort: SpaceRepositoryPort,
    private val currentUser: CurrentUserProvider
) : GetSpacesUseCase {

    @Transactional(readOnly = true)
    override fun execute(): List<Space> {
        return spaceRepositoryPort.findAllByUserId(currentUser.id)
    }
}