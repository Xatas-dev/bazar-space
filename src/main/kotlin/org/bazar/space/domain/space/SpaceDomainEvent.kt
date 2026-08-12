package org.bazar.space.domain.space

sealed interface SpaceDomainEvent {
    val spaceId: Long

    data class Deleted(override val spaceId: Long) : SpaceDomainEvent
}
