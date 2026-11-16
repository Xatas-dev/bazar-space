package org.bazar.space.domain.exception

import java.util.UUID

class SpaceAvatarNotFoundException(fileId: UUID)
    : DomainException("Space avatar not found by fileId: $fileId")