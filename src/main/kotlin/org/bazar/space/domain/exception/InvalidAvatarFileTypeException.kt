package org.bazar.space.domain.exception

class InvalidAvatarFileTypeException(val fileType: String)
    : DomainException("$fileType is not allowed")