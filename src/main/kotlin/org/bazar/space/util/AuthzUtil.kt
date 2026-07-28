package org.bazar.space.util

import org.bazar.authorization.sdk.AuthorizationRequest
import org.bazar.authorization.sdk.Permission

fun buildAuthorizationRequest(
    spaceId: Long,
    permission: Permission,
    jwtToken: String,
    resourceId: String = "",
    principalAttributes: Map<String, String> = emptyMap(),
    resourceAttributes: Map<String, String> = emptyMap()
): AuthorizationRequest =
    AuthorizationRequest.builder()
        .spaceId(spaceId)
        .permission(permission)
        .resourceId(resourceId)
        .principalAttributes(principalAttributes)
        .resourceAttributes(resourceAttributes)
        .bearerToken(jwtToken)
        .build()
