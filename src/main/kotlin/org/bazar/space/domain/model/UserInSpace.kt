package org.bazar.space.domain.model

import java.util.UUID

data class UserInSpace(
    val userId: UUID,
    val spaceId: Long,
    val userName: String,
    val firstName: String,
    val lastName: String,
    val role: RoleInfo?
)

data class RoleInfo(
    val id: Long,
    val name: String,
    val isVisible: Boolean
)
