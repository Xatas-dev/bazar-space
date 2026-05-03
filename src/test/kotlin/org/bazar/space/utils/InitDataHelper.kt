package org.bazar.space.utils

import org.bazar.space.persistence.entity.Space
import org.bazar.space.persistence.entity.UserSpace
import org.bazar.space.persistence.repository.SpaceRepository
import org.bazar.space.persistence.repository.UserSpaceRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class InitDataHelper(
    private val userSpaceRepository: UserSpaceRepository,
    private val spaceRepository: SpaceRepository
) {

    fun createSpace(): Long {
        return spaceRepository.save(Space(name = UUID.randomUUID().toString())).id!!
    }

    fun createUser(spaceId: Long): UserSpace {
        return createUser(spaceId, UUID.randomUUID())
    }

    fun createUser(spaceId: Long, userId: UUID): UserSpace {
        return userSpaceRepository.save(
            UserSpace(spaceId = spaceId, userId = userId)
        )
    }

}