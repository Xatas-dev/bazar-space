package org.bazar.space.infrastructure.authz

import io.github.oshai.kotlinlogging.KotlinLogging
import org.bazar.authorization.sdk.AuthorizationRequest
import org.bazar.space.domain.port.Authorizer
import org.bazar.space.domain.port.AuthzManager
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.stereotype.Service
import java.util.*

@Service
@ConditionalOnMissingBean(SpaceAuthorizationServiceImpl::class)
class MockSpaceAuthorizationService : Authorizer, AuthzManager {

    private val logger = KotlinLogging.logger { }

    init {
        logger.info { "Using mock authorization" }
    }

    override fun authorizeOrThrow(request: AuthorizationRequest) {
        logger.info { "authorization call with request $request" }
    }

    override fun createUserInAuthz(userId: UUID, spaceId: Long, creator: Boolean) {
        logger.info { "creating user in authorization userId: $userId, spaceId: $spaceId, creator: $creator" }
    }

    override fun deleteUserInAuthz(userId: UUID, spaceId: Long, isCreator: Boolean) {
        logger.info { "deleting user in authorization userId: $userId, spaceId: $spaceId" }
    }

    override fun deleteSpaceInAuthz(spaceId: Long) {
        logger.info { "deleting space in authorization spaceId: $spaceId" }
    }
}
