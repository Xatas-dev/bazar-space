package org.bazar.space.domain.exception

sealed class DomainException(
    techMessage: String,
    cause: Throwable? = null,
) : RuntimeException(techMessage, cause)