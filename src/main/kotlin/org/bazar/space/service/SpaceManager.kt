package org.bazar.space.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.bazar.authorization.sdk.AuthorizationException
import org.bazar.authorization.sdk.BazarAuthorizationAdminClient
import org.bazar.space.model.GetSpaceDto
import org.bazar.space.util.buildCreateUserRequest
import org.bazar.space.util.buildDeleteSpaceRequest
import org.bazar.space.util.buildDeleteUserRequest
import org.bazar.space.util.extension.toApiException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class SpaceManager(
    private val spaceService: SpaceService,
    private val userSpaceService: UserSpaceService,
    private val bazarAuthorizationAdminClient: BazarAuthorizationAdminClient
) {

    private val logger = KotlinLogging.logger { }

    @Transactional
    fun createSpace(userId: UUID, name: String): GetSpaceDto {
        val spaceDto = spaceService.createSpace(name)
        userSpaceService.addUserToSpace(spaceDto.id, userId)
        try {
            bazarAuthorizationAdminClient.createUser(buildCreateUserRequest(userId, spaceDto.id, true))
        } catch (ex: AuthorizationException) {
            logger.error(ex) { "Error during adding new space owner to authorization, spaceId = ${spaceDto.id}" }
            throw ex.toApiException()
        }
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
        try {
            bazarAuthorizationAdminClient.createUser(buildCreateUserRequest(userToAddId, spaceId, false))
        } catch (ex: AuthorizationException) {
            logger.error(ex) { "Error during adding new user to authorization, spaceId = $spaceId" }
            throw ex.toApiException()
        }
        logger.info { "Added user=$userToAddId to space=$spaceId" }
    }

    @Transactional
    fun deleteUserFromSpace(spaceId: Long, userId: UUID) {
        userSpaceService.deleteUserFromSpace(spaceId, userId)
        try {
            bazarAuthorizationAdminClient.deleteUser(buildDeleteUserRequest(userId, spaceId))
        } catch (ex: AuthorizationException) {
            logger.error(ex) { "Error during deleting user=$userId from space=$spaceId" }
            throw ex.toApiException()
        }
        logger.info { "Deleted user=$userId from space=$spaceId" }
    }

    @Transactional
    fun deleteSpace(spaceId: Long) {
        userSpaceService.deleteAllBySpaceId(spaceId)
        spaceService.deleteSpaceById(spaceId)
        try {
            bazarAuthorizationAdminClient.deleteSpace(buildDeleteSpaceRequest(spaceId))
        } catch (ex: AuthorizationException) {
            logger.error(ex) { "Error during deleting space $spaceId" }
            throw ex.toApiException()
        }
        logger.info { "Deleted space=$spaceId" }
    }

    @Transactional
    fun updateSpace(spaceId: Long, name: String): GetSpaceDto {
        return spaceService.patchSpace(spaceId, name)
    }

}