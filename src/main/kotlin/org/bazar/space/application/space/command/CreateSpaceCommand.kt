package org.bazar.space.application.space.command

import java.util.UUID

data class CreateSpaceCommand(
    val userId: UUID,
    val name: String
)