package org.bazar.space.domain.port

import org.bazar.authorization.sdk.AuthorizationRequest

interface Authorizer {
    fun authorizeOrThrow(request: AuthorizationRequest)
}
