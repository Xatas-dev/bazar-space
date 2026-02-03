package org.bazar.space.util.exceptions

import org.springframework.http.HttpStatus


class ApiException(
    val exceptionType: ApiExceptions,
    vararg params: Any
) : RuntimeException(formatMessage(exceptionType, params)) {

    companion object {
        fun formatMessage(type: ApiExceptions, args: Array<out Any>): String {
            return if (args.isEmpty()) {
                type.message
            } else {
                try {
                    String.format(type.message, *args)
                } catch (e: Exception) {
                    "${type.message} [Args: ${args.joinToString()}]"
                }
            }
        }
    }

}

enum class ApiExceptions(
    val message: String,
    val httpStatus: HttpStatus
) {
    //business
    AUTHORIZATION_SERVICE_ERROR("Authorization server error", HttpStatus.INTERNAL_SERVER_ERROR),
    FORBIDDEN("Insufficient permissions for this action", HttpStatus.FORBIDDEN),
    NOT_AUTHENTICATED("Not authenticated", HttpStatus.UNAUTHORIZED),
    SPACE_NOT_FOUND("Space %s not found", HttpStatus.UNAUTHORIZED),

    //grpc
    NOT_AUTHENTICATED_GRPC("(grpc) User is not authenticated", HttpStatus.UNAUTHORIZED),
    FORBIDDEN_GRPC("(grpc) Not enough permissions", HttpStatus.FORBIDDEN),
    INTERNAL_ERROR_GRPC("(grpc) Unknown internal error. ", HttpStatus.INTERNAL_SERVER_ERROR),
    NOT_FOUND_GRPC("(grpc) Not found", HttpStatus.NOT_FOUND)
}



