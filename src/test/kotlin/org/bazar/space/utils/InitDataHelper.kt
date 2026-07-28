package org.bazar.space.utils

import org.bazar.space.infrastructure.persistence.entity.SpaceEntity
import org.bazar.space.infrastructure.persistence.entity.UserSpaceEntity
import org.bazar.space.infrastructure.persistence.repository.SpaceEntityRepository
import org.bazar.space.infrastructure.persistence.repository.UserSpaceEntityRepository
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
