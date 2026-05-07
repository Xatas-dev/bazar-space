package org.bazar.space.service.authorization

import io.github.oshai.kotlinlogging.KotlinLogging
import org.bazar.authorization.sdk.AuthorizationException
import org.bazar.authorization.sdk.AuthorizationRequest
import org.bazar.authorization.sdk.BazarAuthorizationAdminClient
import org.bazar.authorization.sdk.BazarAuthorizationClient
import org.bazar.space.util.buildCreateUserRequest
import org.bazar.space.util.buildDeleteSpaceRequest
import org.bazar.space.util.buildDeleteUserRequest
import org.bazar.space.util.exceptions.ApiException
import org.bazar.space.util.exceptions.ApiExceptions
import org.bazar.space.util.extension.toApiException
import org.bazar.space.util.getAuthenticatedUserIdOrThrow
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.util.*

@Service
@Profile("!local && !test")
class SpaceAuthorizationServiceImpl(
    private val bazarAuthzClient: BazarAuthorizationClient,
    private val bazarAuthzAdminClient: BazarAuthorizationAdminClient
) : SpaceAuthorizationService {
    private val logger = KotlinLogging.logger { }

    override fun authorizeOrThrow(request: AuthorizationRequest) {
        val isAuthorized = try {
            bazarAuthzClient.authorize(request)
        } catch (ex: Exception) {
            logger.error(ex) { "Authorization service call failed for spaceId: ${request.spaceId}" }
            throw ApiException(ApiExceptions.AUTHORIZATION_SERVICE_ERROR)
        }

        if (!isAuthorized) {
            logger.warn {
                "Authorization DENIED: permission=${request.permission}, user=${getAuthenticatedUserIdOrThrow()}, space=${request.spaceId}"
            }
            throw ApiException(ApiExceptions.FORBIDDEN)
        }

        logger.debug {
            "Authorization GRANTED: permission=${request.permission}, user=${getAuthenticatedUserIdOrThrow()}, space=${request.spaceId}"
        }
    }

    override fun createUserInAuthz(userId: UUID, spaceId: Long, creator: Boolean) {
        try {
            bazarAuthzAdminClient.createUser(buildCreateUserRequest(userId, spaceId, creator))
        } catch (ex: AuthorizationException) {
            logger.error(ex) { "Error during adding new space owner to authorization, spaceId = $spaceId" }
            throw ex.toApiException()
        }
    }

    override fun deleteUserInAuthz(userId: UUID, spaceId: Long, isCreator: Boolean) {
        try {
            bazarAuthzAdminClient.deleteUser(buildDeleteUserRequest(userId, spaceId, isCreator))
        } catch (ex: AuthorizationException) {
            logger.error(ex) { "Error during deleting user=$userId from space=$spaceId" }
            throw ex.toApiException()
        }
    }

    override fun deleteSpaceInAuthz(spaceId: Long) {
        try {
            bazarAuthzAdminClient.deleteSpace(buildDeleteSpaceRequest(spaceId))
        } catch (ex: AuthorizationException) {
            logger.error(ex) { "Error during deleting space $spaceId" }
            throw ex.toApiException()
        }
    }

}