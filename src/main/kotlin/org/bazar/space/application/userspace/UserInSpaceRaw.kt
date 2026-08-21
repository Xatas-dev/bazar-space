package org.bazar.space.application.userspace

import java.util.UUID

data class UserInSpaceRaw(
    val userId: UUID,
    val spaceId: Long,
    val creator: Boolean
)
