package org.bazar.space.model.http.request

import java.util.UUID

data class AddUserToSpaceDtoRequest(
    val userId: UUID,
    val spaceId: Long
)
