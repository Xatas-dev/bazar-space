package org.bazar.space.domain.exception

class InvalidAvatarFileExtensionException(val fileExtension: String)
    : DomainException("$fileExtension is not allowed")