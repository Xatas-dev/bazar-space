package org.bazar.space.application.space.command

data class UpdateSpaceCommand(
    val spaceId: Long,
    val name: String
)