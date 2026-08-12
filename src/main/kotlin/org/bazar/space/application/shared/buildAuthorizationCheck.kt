package org.bazar.space.application.shared

import org.bazar.space.domain.auth.AuthorizationCheck
import org.bazar.space.domain.auth.Permission

fun buildAuthorizationCheck(
    spaceId: Long,
    permission: Permission,
    resourceId: String = "",
    principalAttributes: Map<String, String> = emptyMap(),
    resourceAttributes: Map<String, String> = emptyMap()
): AuthorizationCheck =
    AuthorizationCheck(
        spaceId = spaceId,
        permission = permission,
        resourceId = resourceId,
        principalAttributes = principalAttributes,
        resourceAttributes = resourceAttributes
    )
