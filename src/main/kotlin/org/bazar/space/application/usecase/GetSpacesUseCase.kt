package org.bazar.space.application.usecase

import org.bazar.space.domain.model.Space
import org.bazar.space.domain.port.SpaceRepositoryPort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class GetSpacesUseCase(
    private val spaceRepositoryPort: SpaceRepositoryPort
) {

    @Transactional(readOnly = true)
    fun execute(userId: UUID): List<Space> {
        return spaceRepositoryPort.findAllByUserId(userId)
    }
}
