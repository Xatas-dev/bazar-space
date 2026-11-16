package org.bazar.space.adapter.inbound.rest.error

import org.springframework.http.HttpStatus

data class HttpError(
    val status: HttpStatus,
    val body: ApiErrorResponse
)