package org.bazar.space.adapter.outbound.authz

import org.bazar.authorization.sdk.CreateUserRequest
import org.bazar.authorization.sdk.DeleteSpaceRequest
import org.bazar.authorization.sdk.DeleteUserRequest
import java.util.UUID

fun buildCreateUserRequest(userId: UUID, spaceId: Long, creator: Boolean, accessToken: String): CreateUserRequest =
    CreateUserRequest.builder()
        .userId(userId.toString())
        .spaceId(spaceId)
        .creator(creator)
        .bearerToken(accessToken)
        .build()

fun buildDeleteUserRequest(userId: UUID, spaceId: Long, isCreator: Boolean, accessToken: String): DeleteUserRequest =
    DeleteUserRequest.builder()
        .userId(userId.toString())
        .spaceId(spaceId)
        .isCreator(isCreator)
        .bearerToken(accessToken)
        .build()

fun buildDeleteSpaceRequest(spaceId: Long, accessToken: String): DeleteSpaceRequest =
    DeleteSpaceRequest.builder()
        .spaceId(spaceId)
        .bearerToken(accessToken)
        .build()
