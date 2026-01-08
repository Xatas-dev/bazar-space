package org.bazar.space.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.bazar.space.config.authorization.client.AuthorizationGrpcClient
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class SpaceManager(
    private val spaceService: SpaceService,
    private val userSpaceService: UserSpaceService,
    private val authorizationGrpcClient: AuthorizationGrpcClient
) {

    private val logger = KotlinLogging.logger { }

    @Transactional
    fun createSpace(userId: UUID): Long {
        val spaceId = spaceService.createSpace()
        userSpaceService.addUserToSpace(spaceId, userId)
        authorizationGrpcClient.tryToCreateSpaceInAuthz(spaceId)
        logger.info { "Created space=$spaceId" }
        return spaceId
    }

    @Transactional
    fun getAllUserSpaces(authenticatedUserId: UUID) = spaceService.getAllUserSpaces(authenticatedUserId)

    @Transactional
    fun getAllUsersInSpace(spaceId: Long) = userSpaceService.getAllUsersInSpace(spaceId)

    @Transactional
    fun addUserToSpace(userToAddId: UUID, spaceId: Long) {
        userSpaceService.addUserToSpace(spaceId, userToAddId)
        authorizationGrpcClient.tryToAddUserToSpaceInAuthz(spaceId, userToAddId)
        logger.info { "Added user=$userToAddId to space=$spaceId" }
    }

    @Transactional
    fun deleteUserFromSpace(spaceId: Long, userId: UUID){
        userSpaceService.deleteUserFromSpace(spaceId, userId)
        authorizationGrpcClient.tryToDeleteUserFromSpaceInAuthz(spaceId, userId)
        logger.info { "Deleted user=$userId from space=$spaceId" }
    }

    @Transactional
    fun deleteSpace(spaceId: Long) {
        userSpaceService.deleteAllBySpaceId(spaceId)
        spaceService.deleteSpaceById(spaceId)
        authorizationGrpcClient.tryToDeleteSpaceInAuthz(spaceId)
        logger.info { "Deleted space=$spaceId" }
    }

}