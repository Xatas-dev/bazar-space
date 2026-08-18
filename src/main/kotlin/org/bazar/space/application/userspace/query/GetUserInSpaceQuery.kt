package org.bazar.space.application.userspace.query

import java.util.UUID

data class GetUserInSpaceQuery(
    val spaceId: Long,
    val userId: UUID,
    val authenticatedUserId: UUID
)
