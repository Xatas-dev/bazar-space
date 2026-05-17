package org.bazar.space.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.bazar.space.model.GetSpaceDto
import org.bazar.space.service.authorization.SpaceAuthorizationService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class SpaceAdminApiService(
    private val spaceService: SpaceService,
    private val userSpaceService: UserSpaceService,
    private val spaceAuthorizationService: SpaceAuthorizationService
) {

    private val logger = KotlinLogging.logger { }

    //Todo: Подумать как решить dual write problem, https://grinbog015.atlassian.net/browse/BZR-113
    @Transactional
    fun createSpace(userId: UUID, name: String): GetSpaceDto {
        val spaceDto = spaceService.createSpace(name)
        userSpaceService.addUserToSpace(spaceDto.id, userId, true)
        spaceAuthorizationService.createUserInAuthz(userId, spaceDto.id, true)
        logger.info { "Created space=$spaceDto" }
        return spaceDto
    }

    @Transactional
    fun addUserToSpace(userToAddId: UUID, spaceId: Long) {
        userSpaceService.addUserToSpace(spaceId, userToAddId, false)
        spaceAuthorizationService.createUserInAuthz(userToAddId, spaceId, false)
        logger.info { "Added user=$userToAddId to space=$spaceId" }
    }

    @Transactional
    fun deleteUserFromSpace(spaceId: Long, userId: UUID) {
        val user = userSpaceService.getUserInSpace(spaceId, userId)
        userSpaceService.deleteUserFromSpace(spaceId, userId)
        spaceAuthorizationService.deleteUserInAuthz(userId, spaceId, user.creator)
        logger.info { "Deleted user=$userId from space=$spaceId" }
    }

    @Transactional
    fun deleteSpace(spaceId: Long) {
        userSpaceService.deleteAllBySpaceId(spaceId)
        spaceService.deleteSpaceById(spaceId)
        spaceAuthorizationService.deleteSpaceInAuthz(spaceId)
        logger.info { "Deleted space=$spaceId" }
    }

    @Transactional
    fun updateSpace(spaceId: Long, name: String): GetSpaceDto {
        return spaceService.patchSpace(spaceId, name)
    }

}