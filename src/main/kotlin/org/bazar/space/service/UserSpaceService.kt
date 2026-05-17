package org.bazar.space.service

import org.bazar.space.persistence.entity.UserSpace
import org.bazar.space.persistence.repository.UserSpaceRepository
import org.bazar.space.util.exceptions.ApiException
import org.bazar.space.util.exceptions.ApiExceptions
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class UserSpaceService(
    private final val userSpaceRepository: UserSpaceRepository
) {

    @Transactional
    fun addUserToSpace(spaceId: Long, userId: UUID, creator: Boolean) {
        val entity = UserSpace(spaceId = spaceId, userId = userId, creator = creator)
        userSpaceRepository.save(entity)
    }

    @Transactional
    fun deleteAllBySpaceId(spaceId: Long) {
        userSpaceRepository.deleteAllBySpaceId(spaceId)
    }

    @Transactional
    fun deleteUserFromSpace(spaceId: Long, userId: UUID) {
        userSpaceRepository.deleteBySpaceIdAndUserId(spaceId, userId)
    }

    @Transactional
    fun getAllUsersInSpace(spaceId: Long) = userSpaceRepository.findAllBySpaceId(spaceId)
        .map { it.userId }.toList()

    @Transactional
    fun getUserInSpace(spaceId: Long, userId: UUID): UserSpace {
        return userSpaceRepository.findBySpaceIdAndUserId(spaceId, userId).getOrNull() ?: throw ApiException(
            ApiExceptions.USER_NOT_FOUND,
            userId,
            spaceId
        )
    }
}