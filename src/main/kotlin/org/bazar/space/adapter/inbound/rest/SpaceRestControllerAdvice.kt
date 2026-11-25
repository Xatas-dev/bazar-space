package org.bazar.space.adapter.inbound.rest

import io.github.oshai.kotlinlogging.KotlinLogging
import org.bazar.space.adapter.inbound.rest.error.ApiErrorResponse
import org.bazar.space.adapter.inbound.rest.error.toHttpError
import org.bazar.space.domain.exception.DomainException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class SpaceRestControllerAdvice {

    private val logger = KotlinLogging.logger { }

    @ExceptionHandler(DomainException::class)
    fun handleDomain(ex: DomainException): ResponseEntity<ApiErrorResponse> {
        logger.error(ex) { "Error while processing request" }
        val httpError = ex.toHttpError()
        return ResponseEntity.status(httpError.status).body(httpError.body)
    }

    @ExceptionHandler(Exception::class)
    fun handleUnexpected(ex: Exception): ResponseEntity<ApiErrorResponse> {
        logger.error(ex) { "Error while processing request" }
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiErrorResponse("INTERNAL_ERROR", "Internal server error"))
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleNotReadable(ex: HttpMessageNotReadableException): ResponseEntity<ApiErrorResponse> {
        logger.warn(ex) { "Malformed request body" }
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiErrorResponse("VALIDATION_ERROR", "Ошибка валидации"))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException): ResponseEntity<ApiErrorResponse> {
        logger.warn(ex) { "Validation failed" }
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiErrorResponse("VALIDATION_ERROR", "Ошибка валидации"))
    }
}