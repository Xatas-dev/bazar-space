package org.bazar.space.utils

import org.bazar.space.adapter.outbound.space.persistence.SpaceEntity
import org.bazar.space.adapter.outbound.space.persistence.SpaceEntityRepository
import org.bazar.space.adapter.outbound.userspace.persistence.UserSpaceEntity
import org.bazar.space.adapter.outbound.userspace.persistence.UserSpaceEntityRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class InitDataHelper(
    private val userSpaceEntityRepository: UserSpaceEntityRepository,
    private val spaceEntityRepository: SpaceEntityRepository
) {

    fun createSpace(): Long {
        return spaceEntityRepository.save(SpaceEntity(name = UUID.randomUUID().toString())).id!!
    }

    fun createUser(spaceId: Long): UserSpaceEntity {
        return createUser(spaceId, UUID.randomUUID())
    }

    fun createUser(spaceId: Long, userId: UUID, creator: Boolean = false): UserSpaceEntity {
        return userSpaceEntityRepository.save(
            UserSpaceEntity(spaceId = spaceId, userId = userId, creator = creator)
        )
    }

}
