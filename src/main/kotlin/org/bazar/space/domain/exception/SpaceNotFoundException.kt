package org.bazar.space.domain.exception

class SpaceNotFoundException : DomainException {

    constructor(spaceId: Long) : super("Space $spaceId not found")

    constructor(joinCode: String) : super("Space by code: $joinCode not found")
}
