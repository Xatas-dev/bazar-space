package org.bazar.space.adapter.outbound.authz

import io.github.oshai.kotlinlogging.KotlinLogging
import org.bazar.authorization.sdk.BazarAuthorizationClient
import org.bazar.space.domain.auth.AuthorizationCheck
import org.bazar.space.application.shared.port.out.Authorizer
import org.bazar.space.application.shared.port.out.CurrentUserProvider
import org.bazar.space.domain.exception.DomainException
import org.bazar.space.domain.exception.DomainErrors
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile("!local && !test")
class AuthorizerAdapter(
    private val bazarAuthzClient: BazarAuthorizationClient,
    private val currentUser: CurrentUserProvider
) : Authorizer {
    private val logger = KotlinLogging.logger { }

    override fun authorizeOrThrow(check: AuthorizationCheck) {
        val isAuthorized = try {
            bazarAuthzClient.authorize(check.toSdkRequest(currentUser.accessToken))
        } catch (ex: Exception) {
            logger.error(ex) { "Authorization service call failed for spaceId: ${check.spaceId}" }
            throw RuntimeException("Authorization server error", ex)
        }

        if (!isAuthorized) {
            logger.warn {
                "Authorization DENIED: permission=${check.permission}, user=${currentUser.id}, space=${check.spaceId}"
            }
            throw DomainException(DomainErrors.FORBIDDEN)
        }

        logger.debug {
            "Authorization GRANTED: permission=${check.permission}, user=${currentUser.id}, space=${check.spaceId}"
        }
    }
}