package org.bazar.space.service

import jakarta.transaction.Transactional
import org.bazar.space.entity.Space
import org.bazar.space.entity.UserSpace
import org.bazar.space.repository.UserSpaceRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserSpaceService(
    private final val userSpaceRepository: UserSpaceRepository
) {

    @Transactional
    fun addUserToSpace(spaceId: Long, userId: UUID) {
        val entity = UserSpace(space = Space(spaceId), userId = userId);
        userSpaceRepository.save(entity)
    }

    @Transactional
    fun deleteAllBySpaceId(spaceId: Long){
        userSpaceRepository.deleteAllBySpaceId(spaceId)
    }

    @Transactional
    fun deleteUserFromSpace(spaceId: Long, userId: UUID){
        userSpaceRepository.deleteBySpaceIdAndUserId(spaceId, userId)
    }

    @Transactional
    fun getAllUsersInSpace(spaceId: Long) = userSpaceRepository.findAllBySpaceId(spaceId)
        .map { it.userId }.toList()

}