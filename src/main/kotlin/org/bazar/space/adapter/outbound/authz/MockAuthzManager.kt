package org.bazar.space.adapter.outbound.authz

import io.github.oshai.kotlinlogging.KotlinLogging
import org.bazar.space.application.shared.port.out.AuthzManager
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.stereotype.Service
import java.util.*

@Service
@ConditionalOnMissingBean(AuthzManagerAdapter::class)
class MockAuthzManager : AuthzManager {

    private val logger = KotlinLogging.logger { }

    init {
        logger.info { "Using mock authz manager" }
    }

    override fun createUserInAuthz(userId: UUID, spaceId: Long, creator: Boolean) {
        logger.info { "Creating user in authz: userId=$userId, spaceId=$spaceId, creator=$creator" }
    }

    override fun deleteUserInAuthz(userId: UUID, spaceId: Long, isCreator: Boolean) {
        logger.info { "Deleting user in authz: userId=$userId, spaceId=$spaceId" }
    }

    override fun deleteSpaceInAuthz(spaceId: Long) {
        logger.info { "Deleting space in authz: spaceId=$spaceId" }
    }
}
