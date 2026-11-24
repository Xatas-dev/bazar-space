package org.bazar.space.application.joinrequest.command

import java.util.UUID

data class CreateJoinRequestCommand(
    val joinCode: String,
    val userId: UUID
)
