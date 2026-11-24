package org.bazar.space.domain.exception

import java.util.UUID

class UserAlreadyInSpaceException(userId: UUID, spaceId: Long)
    : DomainException("User $userId is already a member of space $spaceId")
