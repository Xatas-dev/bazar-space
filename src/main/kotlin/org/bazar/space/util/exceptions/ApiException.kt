package org.bazar.space.util.exceptions

import io.netty.handler.codec.http.HttpResponseStatus
import io.netty.handler.codec.http.HttpStatusClass
import org.springframework.http.HttpStatus


class ApiException(
    val exceptionType: ApiExceptions,
    customMessage: String = ""
) : RuntimeException(exceptionType.message + customMessage) {

    val errorMessage = exceptionType.message + customMessage
}

enum class ApiExceptions(
    val message: String,
    val httpStatus: HttpStatus
) {
    //business
    AUTHORIZATION_SERVICE_ERROR("Authorization server error", HttpStatus.INTERNAL_SERVER_ERROR),
    FORBIDDEN("Insufficient permissions for this action", HttpStatus.FORBIDDEN),
    NOT_AUTHENTICATED("Not authenticated", HttpStatus.UNAUTHORIZED),
    //grpc
    NOT_AUTHENTICATED_GRPC("(grpc) User is not authenticated", HttpStatus.UNAUTHORIZED),
    FORBIDDEN_GRPC("(grpc) Not enough permissions", HttpStatus.FORBIDDEN),
    INTERNAL_ERROR_GRPC("(grpc) Unknown internal error. ", HttpStatus.INTERNAL_SERVER_ERROR),
    NOT_FOUND_GRPC("(grpc) Not found", HttpStatus.NOT_FOUND)
}



