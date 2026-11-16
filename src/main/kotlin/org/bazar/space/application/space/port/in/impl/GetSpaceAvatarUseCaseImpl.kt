package org.bazar.space.application.space.port.`in`.impl

import org.bazar.space.application.space.port.`in`.GetSpaceAvatarUseCase
import org.bazar.space.application.space.port.out.SpaceAvatarRepositoryPort
import org.bazar.space.domain.space.SpaceAvatar
import org.springframework.stereotype.Service

@Service
class GetSpaceAvatarUseCaseImpl(
    private val spaceAvatarRepositoryPort: SpaceAvatarRepositoryPort
) : GetSpaceAvatarUseCase {

    override fun execute(spaceId: Long): List<SpaceAvatar> =
        spaceAvatarRepositoryPort.findAllBySpaceId(spaceId)
}