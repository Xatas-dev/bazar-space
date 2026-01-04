package org.bazar.space.util.extension

import io.grpc.Status.Code.*
import io.grpc.StatusRuntimeException
import org.bazar.space.util.exceptions.ApiException
import org.bazar.space.util.exceptions.ApiExceptions.*

fun StatusRuntimeException.toApiException(): ApiException {
    return when (this.status.code) {
        UNAUTHENTICATED -> ApiException(NOT_AUTHENTICATED_GRPC)
        PERMISSION_DENIED -> ApiException(FORBIDDEN_GRPC)
        NOT_FOUND -> ApiException(NOT_FOUND_GRPC)
        else -> ApiException(INTERNAL_ERROR_GRPC)
    }
}