package org.bazar.space.application.userspace.command

import java.util.UUID

data class DeleteUserFromSpaceCommand(
    val spaceId: Long,
    val userId: UUID
)