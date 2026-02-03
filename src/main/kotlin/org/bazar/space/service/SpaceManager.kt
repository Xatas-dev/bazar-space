package org.bazar.space.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.bazar.space.config.authorization.client.AuthorizationGrpcClient
import org.bazar.space.model.GetSpaceDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class SpaceManager(
    private val spaceService: SpaceService,
    private val userSpaceService: UserSpaceService,
    private val authorizationGrpcClientImpl: AuthorizationGrpcClient
) {

    private val logger = KotlinLogging.logger { }

    @Transactional
    fun createSpace(userId: UUID, name: String): GetSpaceDto {
        val spaceDto = spaceService.createSpace(name)
        userSpaceService.addUserToSpace(spaceDto.id, userId)
        authorizationGrpcClientImpl.tryToCreateSpaceInAuthz(spaceDto.id)
        logger.info { "Created space=$spaceDto" }
        return spaceDto
    }

    @Transactional
    fun getAllSpacesByUserId(authenticatedUserId: UUID) = spaceService.getAllUserSpaces(authenticatedUserId)

    @Transactional
    fun getAllUsersInSpace(spaceId: Long) = userSpaceService.getAllUsersInSpace(spaceId)

    @Transactional
    fun addUserToSpace(userToAddId: UUID, spaceId: Long) {
        userSpaceService.addUserToSpace(spaceId, userToAddId)
        authorizationGrpcClientImpl.tryToAddUserToSpaceInAuthz(spaceId, userToAddId)
        logger.info { "Added user=$userToAddId to space=$spaceId" }
    }

    @Transactional
    fun deleteUserFromSpace(spaceId: Long, userId: UUID) {
        userSpaceService.deleteUserFromSpace(spaceId, userId)
        authorizationGrpcClientImpl.tryToDeleteUserFromSpaceInAuthz(spaceId, userId)
        logger.info { "Deleted user=$userId from space=$spaceId" }
    }

    @Transactional
    fun deleteSpace(spaceId: Long) {
        userSpaceService.deleteAllBySpaceId(spaceId)
        spaceService.deleteSpaceById(spaceId)
        authorizationGrpcClientImpl.tryToDeleteSpaceInAuthz(spaceId)
        logger.info { "Deleted space=$spaceId" }
    }

    @Transactional
    fun patchSpace(spaceId: Long, name: String): GetSpaceDto {
        return spaceService.patchSpace(spaceId, name)
    }

}