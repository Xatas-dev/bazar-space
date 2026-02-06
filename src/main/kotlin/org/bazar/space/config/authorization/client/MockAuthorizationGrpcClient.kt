package org.bazar.space.config.authorization.client

import org.bazar.space.service.authorization.AuthorizationAction
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.util.UUID

@Profile("local", "test")
@Component
class MockAuthorizationGrpcClient: AuthorizationGrpcClient {
    override fun tryToCreateSpaceInAuthz(spaceId: Long) {

    }

    override fun tryToAddUserToSpaceInAuthz(spaceId: Long, userId: UUID) {
    }

    override fun tryToDeleteUserFromSpaceInAuthz(spaceId: Long, userId: UUID) {
    }

    override fun tryToDeleteSpaceInAuthz(spaceId: Long) {
    }

    override fun checkAccess(
        spaceId: Long,
        action: AuthorizationAction
    ): Boolean = true
}