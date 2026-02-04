package org.bazar.space.config.web.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Profile
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.time.Instant
import java.util.*

@Component
@Profile("local", "test")
class LocalJwtInjectorFilter : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val headers = mapOf("alg" to "none")
        val claims = mapOf(
            "sub" to UUID.fromString("fd71e64e-edb2-4ecb-bc90-7f27c34e0af7"), // The ID your app expects
            "email" to "dev@bazar.com",
            "preferred_username" to "local"
        )
        val jwt = Jwt(
            "mock-token-value",         // tokenValue
            Instant.now(),              // issuedAt
            Instant.now().plusSeconds(360000), // expiresAt
            headers,
            claims
        )
        val authentication = JwtAuthenticationToken(jwt)
        SecurityContextHolder.getContext().authentication = authentication
        filterChain.doFilter(request, response)
    }

}