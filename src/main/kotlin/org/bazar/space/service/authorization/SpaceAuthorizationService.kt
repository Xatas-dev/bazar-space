package org.bazar.space.service.authorization

import io.github.oshai.kotlinlogging.KotlinLogging
import org.bazar.authorization.sdk.AuthorizationRequest
import org.bazar.authorization.sdk.BazarAuthorizationClient
import org.bazar.space.util.exceptions.ApiException
import org.bazar.space.util.exceptions.ApiExceptions
import org.bazar.space.util.getAuthenticatedUserIdOrThrow
import org.springframework.stereotype.Service

@Service
class SpaceAuthorizationService(
    private val bazarAuthzClient: BazarAuthorizationClient
) {
    private val logger = KotlinLogging.logger { }

    fun authorizeOrThrow(request: AuthorizationRequest) {
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
}