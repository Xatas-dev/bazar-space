package org.bazar.space.application.space.port.`in`

import org.bazar.space.domain.space.SpaceAvatar

fun interface GetSpaceAvatarUseCase {
    fun execute(spaceId: Long): List<SpaceAvatar>
}