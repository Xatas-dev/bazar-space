package org.bazar.space.util.exceptions

import org.springframework.http.HttpStatus


class ApiException(
    val exceptionType: ApiExceptions,
    vararg params: Any,
    cause: Exception? = null
) : RuntimeException(formatMessage(exceptionType, params), cause) {

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
    FORBIDDEN("Insufficient permissions for this action", HttpStatus.FORBIDDEN),
    NOT_AUTHENTICATED("Not authenticated", HttpStatus.UNAUTHORIZED),
    SPACE_NOT_FOUND("Space %s not found", HttpStatus.UNAUTHORIZED),
    USER_NOT_FOUND("User not found, userId: %s, spaceId: %s", HttpStatus.NOT_FOUND),

    //external systems
    AUTHORIZATION_SERVICE_ERROR("Authorization server error", HttpStatus.INTERNAL_SERVER_ERROR),
    FORBIDDEN_GRPC("(grpc) Not enough permissions", HttpStatus.FORBIDDEN),
    KAFKA_SENDING_EXCEPTION("Couldn't produce message to kafka topic: %s a record: %s", HttpStatus.INTERNAL_SERVER_ERROR)

}



