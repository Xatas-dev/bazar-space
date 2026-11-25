package org.bazar.space.application.joinrequest

import java.time.Instant
import java.util.UUID

data class JoinRequestWithUser(
    val id: Long,
    val user: JoinRequestUser,
    val createdAt: Instant
)

data class JoinRequestUser(
    val userId: UUID,
    val username: String?,
    val displayName: String?,
    val avatarUrl: String?
)
