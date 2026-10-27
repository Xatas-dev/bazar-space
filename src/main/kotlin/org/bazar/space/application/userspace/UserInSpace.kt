package org.bazar.space.application.userspace

import java.util.UUID

data class UserInSpace(
    val userId: UUID,
    val spaceId: Long,
    val username: String,
    val displayName: String,
    val creator: Boolean,
    val role: RoleInfo?
)

data class RoleInfo(
    val id: Long,
    val name: String,
    val isVisible: Boolean
)
