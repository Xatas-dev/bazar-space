package org.bazar.space.domain.event

sealed interface SpaceDomainEvent {
    val spaceId: Long

    data class Deleted(override val spaceId: Long) : SpaceDomainEvent
}
