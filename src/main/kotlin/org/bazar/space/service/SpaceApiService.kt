package org.bazar.space.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.bazar.space.model.GetUsersInSpaceResponse
import org.bazar.space.util.exceptions.ApiException
import org.bazar.space.util.exceptions.ApiExceptions
import org.bazar.space.util.extension.buildGetUsersInSpaceResponse
import org.bazar.space.util.getAuthenticatedUserIdOrThrow
import org.bazar.space.util.rest.client.BazarAuthorizationHttpClient
import org.bazar.space.util.rest.client.BazarPersonaClient
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class SpaceApiService(
    private val spaceService: SpaceService,
    private val userSpaceService: UserSpaceService,
    private val bazarPersonaClient: BazarPersonaClient,
    private val bazarAuthorizationHttpClient: BazarAuthorizationHttpClient
) {

    private val logger = KotlinLogging.logger { }

    @Transactional
    fun getAllSpacesByUserId(authenticatedUserId: UUID) = spaceService.getAllUserSpaces(authenticatedUserId)

    @Transactional(readOnly = true)
    fun getAllUsersInSpaceEnriched(spaceId: Long): GetUsersInSpaceResponse {

        val userIds = userSpaceService.getAllUsersInSpace(spaceId)

        if (!userIds.contains(getAuthenticatedUserIdOrThrow())) {
            logger.warn { "user with id: ${getAuthenticatedUserIdOrThrow()} is not part of spaceId: $spaceId" }
            throw ApiException(ApiExceptions.FORBIDDEN)
        }

        val userIdToUserInfoMap = bazarPersonaClient.getUsersByIds(userIds)
        val userIdToGetRoleDtoMap = bazarAuthorizationHttpClient.getRoleNames(spaceId, userIds)

        return buildGetUsersInSpaceResponse(spaceId, userIds, userIdToUserInfoMap, userIdToGetRoleDtoMap)
    }
}