package org.bazar.space.domain.port

import java.util.UUID

fun interface UserInfoProvider {
    fun getUsersByIds(userIds: List<UUID>): Map<UUID, UserPersonaDto>
}

data class UserPersonaDto(
    val id: UUID,
    val userName: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val userPic: String? = null
)
