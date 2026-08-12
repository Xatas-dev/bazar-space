package org.bazar.space.application.userspace.command

import java.util.UUID

data class AddUserToSpaceCommand(
    val userToAddId: UUID,
    val spaceId: Long
)