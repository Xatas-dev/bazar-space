package org.bazar.space.application.shared.port.out

import java.util.UUID

interface AuthzManager {
    fun createUserInAuthz(userId: UUID, spaceId: Long, creator: Boolean)

    fun deleteUserInAuthz(userId: UUID, spaceId: Long, isCreator: Boolean)

    fun deleteSpaceInAuthz(spaceId: Long)
}
