package org.bazar.space.application.shared.port.out

import org.bazar.space.domain.auth.AuthorizationCheck

interface Authorizer {
    fun authorizeOrThrow(check: AuthorizationCheck)
}
