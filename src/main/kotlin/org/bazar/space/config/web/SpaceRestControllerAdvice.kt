package org.bazar.space.config.web

import org.bazar.space.util.exceptions.ApiException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class SpaceRestControllerAdvice {

    @ExceptionHandler(ApiException::class)
    fun handleAccessDenied(ex: ApiException): ResponseEntity<ProblemDetail> {
        return ResponseEntity.status(ex.exceptionType.httpStatus)
            .body(ProblemDetail.forStatusAndDetail(ex.exceptionType.httpStatus, ex.errorMessage))
    }

}