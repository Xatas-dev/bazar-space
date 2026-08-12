package org.bazar.space.adapter.outbound.security

import org.bazar.space.application.shared.port.out.CurrentUserProvider
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component
import org.springframework.web.context.annotation.RequestScope
import java.util.*

@RequestScope
@Component
class SpringCurrentUserProvider : CurrentUserProvider {

    private val jwt: Jwt?
        get() = SecurityContextHolder.getContext().authentication?.principal as? Jwt

    override val id: UUID
        get() = jwt?.subject?.let { UUID.fromString(it) }
            ?: throw IllegalStateException("User ID not found in security context")

    override val accessToken: String
        get() = jwt?.tokenValue
            ?: throw IllegalStateException("JWT Token not found in security context")
}
