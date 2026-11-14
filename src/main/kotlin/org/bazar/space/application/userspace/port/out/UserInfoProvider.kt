package org.bazar.space.application.userspace.port.out

import java.util.UUID

fun interface UserInfoProvider {
    fun getUsersByIds(userIds: List<UUID>): Map<UUID, UserPersonaDto>
}

data class UserPersonaDto(
    val id: UUID,
    val username: String,
    val displayName: String,
)
