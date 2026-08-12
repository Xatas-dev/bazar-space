package org.bazar.space.application.shared.port.out

import java.util.UUID

interface CurrentUserProvider {
    val id: UUID
    val accessToken: String
}
