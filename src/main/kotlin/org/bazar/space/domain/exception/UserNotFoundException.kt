package org.bazar.space.domain.exception

import java.util.UUID

class UserNotFoundException(userId: UUID, spaceId: Long)
    : DomainException("User $userId not found in space $spaceId")