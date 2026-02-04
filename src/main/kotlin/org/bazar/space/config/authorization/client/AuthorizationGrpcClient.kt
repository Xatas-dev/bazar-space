package org.bazar.space.config.authorization.client

import org.bazar.space.config.authorization.AuthorizationAction
import java.util.*

interface AuthorizationGrpcClient {
    fun tryToCreateSpaceInAuthz(spaceId: Long)

    fun tryToAddUserToSpaceInAuthz(spaceId: Long, userId: UUID)

    fun tryToDeleteUserFromSpaceInAuthz(spaceId: Long, userId: UUID)

    fun tryToDeleteSpaceInAuthz(spaceId: Long)

    fun checkAccess(spaceId: Long, action: AuthorizationAction): Boolean
}