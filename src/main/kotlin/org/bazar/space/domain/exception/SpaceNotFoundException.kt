package org.bazar.space.domain.exception

class SpaceNotFoundException(spaceId: Long)
    : DomainException("Space $spaceId not found")