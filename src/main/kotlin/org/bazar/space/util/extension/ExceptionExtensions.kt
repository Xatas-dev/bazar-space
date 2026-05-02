package org.bazar.space.util.extension

import org.bazar.authorization.sdk.AuthorizationException
import org.bazar.space.util.exceptions.ApiException
import org.bazar.space.util.exceptions.ApiExceptions.FORBIDDEN_GRPC

fun AuthorizationException.toApiException(): ApiException {
    return ApiException(FORBIDDEN_GRPC)
}