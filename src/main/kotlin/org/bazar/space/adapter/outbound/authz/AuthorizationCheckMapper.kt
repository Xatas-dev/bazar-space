package org.bazar.space.adapter.outbound.authz

import org.bazar.authorization.sdk.AuthorizationRequest
import org.bazar.space.domain.auth.AuthorizationCheck
import org.bazar.space.domain.auth.Permission

fun AuthorizationCheck.toSdkRequest(accessToken: String): AuthorizationRequest =
    AuthorizationRequest.builder()
        .spaceId(spaceId)
        .permission(permission.toSdkPermission())
        .resourceId(resourceId)
        .bearerToken(accessToken)
        .principalAttributes(principalAttributes)
        .resourceAttributes(resourceAttributes)
        .build()

fun Permission.toSdkPermission(): org.bazar.authorization.sdk.Permission = when (this) {
    Permission.SPACE_WRITE -> org.bazar.authorization.sdk.Permission.SPACE_WRITE
    Permission.SPACE_DELETE -> org.bazar.authorization.sdk.Permission.SPACE_DELETE
    Permission.SPACE_USER_ADD -> org.bazar.authorization.sdk.Permission.SPACE_USER_ADD
    Permission.SPACE_USER_DELETE -> org.bazar.authorization.sdk.Permission.SPACE_USER_DELETE
}
