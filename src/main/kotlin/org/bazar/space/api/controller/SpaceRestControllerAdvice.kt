package org.bazar.space.api.controller

import org.bazar.space.util.exceptions.ApiException
import org.bazar.space.util.exceptions.ApiExceptions
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class SpaceRestControllerAdvice {

    @ExceptionHandler(ApiException::class)
    fun handleAccessDenied(ex: ApiException): ResponseEntity<ProblemDetail> {
        val status = ex.exceptionType.toHttpStatus()
        return ResponseEntity.status(status)
            .body(ProblemDetail.forStatusAndDetail(status, ex.message))
    }

    private fun ApiExceptions.toHttpStatus(): HttpStatus = when (this) {
        ApiExceptions.FORBIDDEN -> HttpStatus.FORBIDDEN
        ApiExceptions.NOT_AUTHENTICATED -> HttpStatus.UNAUTHORIZED
        ApiExceptions.SPACE_NOT_FOUND -> HttpStatus.UNAUTHORIZED
        ApiExceptions.USER_NOT_FOUND -> HttpStatus.NOT_FOUND
        ApiExceptions.AUTHORIZATION_SERVICE_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR
        ApiExceptions.FORBIDDEN_GRPC -> HttpStatus.FORBIDDEN
        ApiExceptions.KAFKA_SENDING_EXCEPTION -> HttpStatus.INTERNAL_SERVER_ERROR
    }
}