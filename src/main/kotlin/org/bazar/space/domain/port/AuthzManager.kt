package org.bazar.space.domain.port

import java.util.UUID

interface AuthzManager {
    fun createUserInAuthz(userId: UUID, spaceId: Long, creator: Boolean)

    fun deleteUserInAuthz(userId: UUID, spaceId: Long, isCreator: Boolean)

    fun deleteSpaceInAuthz(spaceId: Long)
}
