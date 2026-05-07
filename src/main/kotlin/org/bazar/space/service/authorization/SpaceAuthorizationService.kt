package org.bazar.space.service.authorization

import org.bazar.authorization.sdk.AuthorizationRequest
import java.util.*

interface SpaceAuthorizationService {
    fun authorizeOrThrow(request: AuthorizationRequest)

    fun createUserInAuthz(userId: UUID, spaceId: Long, creator: Boolean)

    fun deleteUserInAuthz(userId: UUID, spaceId: Long, isCreator: Boolean)

    fun deleteSpaceInAuthz(spaceId: Long)
}