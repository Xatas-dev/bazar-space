package org.bazar.space.application.space.port.`in`.impl

import org.bazar.space.application.space.port.`in`.GetSpacePreviewByJoinCodeUseCase
import org.bazar.space.application.space.port.`in`.SpacePreview
import org.bazar.space.application.space.port.out.SpaceAvatarRepositoryPort
import org.bazar.space.application.space.port.out.SpaceRepositoryPort
import org.bazar.space.domain.exception.SpaceNotFoundException
import org.springframework.stereotype.Service

@Service
class GetSpacePreviewByJoinCodeUseCaseImpl(
    private val spaceRepositoryPort: SpaceRepositoryPort,
    private val spaceAvatarRepositoryPort: SpaceAvatarRepositoryPort
) : GetSpacePreviewByJoinCodeUseCase {

    override fun execute(joinCode: String): SpacePreview {
        val space = spaceRepositoryPort.findByJoinCode(joinCode) ?: throw SpaceNotFoundException(joinCode)
        val spaceId = requireNotNull(space.id) { "Persisted space must have id" }
        return SpacePreview(
            name = space.name,
            avatarUrl = spaceAvatarRepositoryPort.findActiveBySpaceId(spaceId)?.url
        )
    }
}
