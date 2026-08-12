package org.bazar.space.adapter.outbound.authz

import io.github.oshai.kotlinlogging.KotlinLogging
import org.bazar.authorization.sdk.AuthorizationException as SdkAuthorizationException
import org.bazar.authorization.sdk.BazarAuthorizationAdminClient
import org.bazar.space.application.shared.port.out.AuthzManager
import org.bazar.space.application.shared.port.out.CurrentUserProvider
import org.bazar.space.domain.exception.DomainException
import org.bazar.space.domain.exception.DomainErrors
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.util.*

@Service
@Profile("!local && !test")
class AuthzManagerAdapter(
    private val bazarAuthzAdminClient: BazarAuthorizationAdminClient,
    private val currentUser: CurrentUserProvider
) : AuthzManager {
    private val logger = KotlinLogging.logger { }

    override fun createUserInAuthz(userId: UUID, spaceId: Long, creator: Boolean) {
        try {
            bazarAuthzAdminClient.createUser(buildCreateUserRequest(userId, spaceId, creator, currentUser.accessToken))
        } catch (ex: SdkAuthorizationException) {
            logger.error(ex) { "Error during adding new space owner to authz, spaceId = $spaceId" }
            throw DomainException(DomainErrors.FORBIDDEN)
        }
    }

    override fun deleteUserInAuthz(userId: UUID, spaceId: Long, isCreator: Boolean) {
        try {
            bazarAuthzAdminClient.deleteUser(buildDeleteUserRequest(userId, spaceId, isCreator, currentUser.accessToken))
        } catch (ex: SdkAuthorizationException) {
            logger.error(ex) { "Error during deleting user=$userId from space=$spaceId" }
            throw DomainException(DomainErrors.FORBIDDEN)
        }
    }

    override fun deleteSpaceInAuthz(spaceId: Long) {
        try {
            bazarAuthzAdminClient.deleteSpace(buildDeleteSpaceRequest(spaceId, currentUser.accessToken))
        } catch (ex: SdkAuthorizationException) {
            logger.error(ex) { "Error during deleting space $spaceId" }
            throw DomainException(DomainErrors.FORBIDDEN)
        }
    }
}