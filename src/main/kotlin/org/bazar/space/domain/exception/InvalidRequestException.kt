package org.bazar.space.domain.exception

class InvalidRequestException(val details: String) : DomainException("Invalid request: $details")
