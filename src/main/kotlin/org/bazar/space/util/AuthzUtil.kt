package org.bazar.space.util

import org.bazar.authorization.sdk.*
import java.util.*

fun buildAuthorizationRequest(
    spaceId: Long,
    permission: Permission,
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
        .bearerToken(getCurrentJwt())
        .build()

fun buildCreateUserRequest(userId: UUID, spaceId: Long, creator: Boolean): CreateUserRequest =
    CreateUserRequest.builder()
        .userId(userId.toString())
        .spaceId(spaceId)
        .creator(creator)
        .bearerToken(getCurrentJwt())
        .build()

fun buildDeleteUserRequest(userId: UUID, spaceId: Long, isCreator: Boolean): DeleteUserRequest =
    DeleteUserRequest.builder()
        .userId(userId.toString())
        .spaceId(spaceId)
        .isCreator(isCreator)
        .bearerToken(getCurrentJwt())
        .build()

fun buildDeleteSpaceRequest(spaceId: Long): DeleteSpaceRequest =
    DeleteSpaceRequest.builder()
        .spaceId(spaceId)
        .bearerToken(getCurrentJwt())
        .build()