package org.bazar.space.adapter.inbound.rest

import org.bazar.space.domain.exception.DomainException
import org.bazar.space.domain.exception.DomainErrors
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class SpaceRestControllerAdvice {

    @ExceptionHandler(DomainException::class)
    fun handleDomainException(ex: DomainException): ResponseEntity<ProblemDetail> {
        val status = ex.exceptionType.toHttpStatus()
        return ResponseEntity.status(status)
            .body(ProblemDetail.forStatusAndDetail(status, ex.message))
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ProblemDetail> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.message))
    }

    private fun DomainErrors.toHttpStatus(): HttpStatus = when (this) {
        DomainErrors.FORBIDDEN -> HttpStatus.FORBIDDEN
        DomainErrors.NOT_AUTHENTICATED -> HttpStatus.UNAUTHORIZED
        DomainErrors.SPACE_NOT_FOUND -> HttpStatus.NOT_FOUND
        DomainErrors.USER_NOT_FOUND -> HttpStatus.NOT_FOUND
    }
}