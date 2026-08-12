package org.bazar.space.application.userspace.port.out

import java.util.UUID

fun interface RoleInfoProvider {
    fun getRoleNames(spaceId: Long, userIds: List<UUID>): Map<UUID, GetRoleNameDto>
}

data class GetRoleNameDto(
    val id: Long,
    val name: String,
    val userId: String,
    val isVisible: Boolean
)
