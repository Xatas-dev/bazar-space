package org.bazar.space.domain.space

import org.bazar.space.domain.exception.DomainException
import org.bazar.space.domain.exception.DomainErrors
import org.bazar.space.domain.userspace.UserSpace
import java.util.UUID

object SpaceMembership {

    fun ensureUserIsMember(members: List<UserSpace>, userId: UUID) {
        if (members.none { it.userId == userId }) {
            throw DomainException(DomainErrors.FORBIDDEN)
        }
    }

    fun isCreator(members: List<UserSpace>, userId: UUID): Boolean =
        members.any { it.userId == userId && it.creator }
}
