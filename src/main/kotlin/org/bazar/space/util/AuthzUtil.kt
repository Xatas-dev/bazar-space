package org.bazar.space.util

import org.bazar.authorization.sdk.AuthorizationRequest
import org.bazar.authorization.sdk.CreateUserRequest
import org.bazar.authorization.sdk.DeleteSpaceRequest
import org.bazar.authorization.sdk.DeleteUserRequest
import org.bazar.authorization.sdk.Permission
import java.util.*

fun buildAuthorizationRequest(spaceId: Long, permission: Permission): AuthorizationRequest =
    AuthorizationRequest.builder()
        .spaceId(spaceId)
        .permission(permission)
        .bearerToken(getCurrentJwt())
        .build()

fun buildCreateUserRequest(userId: UUID, spaceId: Long, creator: Boolean): CreateUserRequest =
    CreateUserRequest.builder()
        .userId(userId.toString())
        .spaceId(spaceId)
        .creator(creator)
        .bearerToken(getCurrentJwt())
        .build()

fun buildDeleteUserRequest(userId: UUID, spaceId: Long): DeleteUserRequest =
    DeleteUserRequest.builder()
        .userId(userId.toString())
        .spaceId(spaceId)
        .bearerToken(getCurrentJwt())
        .build()

fun buildDeleteSpaceRequest(spaceId: Long): DeleteSpaceRequest =
    DeleteSpaceRequest.builder()
        .spaceId(spaceId)
        .bearerToken(getCurrentJwt())
        .build()