package org.bazar.space.domain.exception

class InvalidAvatarSizeException(maxSize: Long, actualSize: Long)
    : DomainException("Avatar size $actualSize exceeds max size $maxSize")