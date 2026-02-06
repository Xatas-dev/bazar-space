package org.bazar.space.service.authorization

import io.github.oshai.kotlinlogging.KotlinLogging
import org.bazar.space.config.authorization.client.AuthorizationGrpcClient
import org.bazar.space.util.exceptions.ApiException
import org.bazar.space.util.exceptions.ApiExceptions
import org.bazar.space.util.getAuthenticatedUserIdOrThrow
import org.springframework.stereotype.Service

@Service
class SpaceAuthorizationService(
    private val authzClient: AuthorizationGrpcClient
) {
    private val logger = KotlinLogging.logger { }

    fun authorizeOrThrow(spaceId: Long, action: AuthorizationAction) {
        val isAuthorized = try {
            authzClient.checkAccess(spaceId, action)
        } catch (ex: Exception) {
            logger.error(ex) { "Authorization service call failed for spaceId: $spaceId" }
            throw ApiException(ApiExceptions.AUTHORIZATION_SERVICE_ERROR)
        }

        if (!isAuthorized) {
            logger.warn {
                "Authorization DENIED: action=$action, user=${getAuthenticatedUserIdOrThrow()}, space=$spaceId"
            }
            throw ApiException(ApiExceptions.FORBIDDEN)
        }

        logger.debug {
            "Authorization GRANTED: action=$action, user=${getAuthenticatedUserIdOrThrow()}, space=$spaceId"
        }
    }
}

enum class AuthorizationAction(val actionName: String) {
    ADD_USER_TO_SPACE("add_user_to_space"),
    REMOVE_USER_FROM_SPACE("remove_user_from_space"),
    DELETE_SPACE("delete_space"),
    ACCESS_CHAT("access_chat"),
    READ_SPACE("read_space"),
    EDIT_SPACE("edit_space"),
    ACCESS_STORAGE("access_storage");
}