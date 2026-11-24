package org.bazar.space.utils

import org.bazar.space.adapter.outbound.userspace.persistence.UserSpaceEntity
import org.bazar.space.adapter.outbound.userspace.persistence.UserSpaceEntityRepository
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class UserSpaceCreator(
    private val userSpaceEntityRepository: UserSpaceEntityRepository
) {
    fun create(
        spaceId: Long,
        userId: UUID = UUID.randomUUID(),
        creator: Boolean = false,
    ): UserSpaceEntity = userSpaceEntityRepository.save(
        UserSpaceEntity(
            spaceId = spaceId,
            userId = userId,
            creator = creator,
        )
    )
}
