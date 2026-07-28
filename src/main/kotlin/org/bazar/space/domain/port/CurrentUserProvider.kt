package org.bazar.space.domain.port

import java.util.UUID

interface CurrentUserProvider {
    val id: UUID
    val jwtToken: String
}
