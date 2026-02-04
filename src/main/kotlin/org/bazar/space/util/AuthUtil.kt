package org.bazar.space.util

import org.bazar.space.util.exceptions.ApiException
import org.bazar.space.util.exceptions.ApiExceptions
import org.bazar.space.util.extension.toUuid
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import java.util.*

fun getAuthenticatedUserIdOrThrow(): UUID {
    val authentication = SecurityContextHolder.getContext().authentication
        ?: throw ApiException(ApiExceptions.NOT_AUTHENTICATED)
    val jwt = authentication.principal as Jwt
    return jwt.subject.toUuid()
}