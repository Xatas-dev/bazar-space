package org.bazar.space.application.userspace.query

import java.util.UUID

data class GetUsersInSpaceQuery(
    val spaceId: Long,
    val authenticatedUserId: UUID
)