package org.bazar.space.domain.exception


class DomainException(
    val exceptionType: DomainErrors,
    vararg params: Any,
    cause: Exception? = null
) : RuntimeException(formatMessage(exceptionType, params), cause) {

    companion object {
        fun formatMessage(type: DomainErrors, args: Array<out Any>): String {
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

enum class DomainErrors(
    val message: String
) {
    FORBIDDEN("Insufficient permissions for this action"),
    NOT_AUTHENTICATED("Not authenticated"),
    SPACE_NOT_FOUND("Space %s not found"),
    USER_NOT_FOUND("User not found, userId: %s, spaceId: %s")
}
