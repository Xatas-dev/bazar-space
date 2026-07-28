package org.bazar.space.util.exceptions


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
    val message: String
) {
    FORBIDDEN("Insufficient permissions for this action"),
    NOT_AUTHENTICATED("Not authenticated"),
    SPACE_NOT_FOUND("Space %s not found"),
    USER_NOT_FOUND("User not found, userId: %s, spaceId: %s"),

    AUTHORIZATION_SERVICE_ERROR("Authorization server error"),
    FORBIDDEN_GRPC("(grpc) Not enough permissions"),
    KAFKA_SENDING_EXCEPTION("Couldn't produce message to kafka topic: %s a record: %s")

}



