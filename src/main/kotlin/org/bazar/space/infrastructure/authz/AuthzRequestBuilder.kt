package org.bazar.space.infrastructure.authz

import org.bazar.authorization.sdk.CreateUserRequest
import org.bazar.authorization.sdk.DeleteSpaceRequest
import org.bazar.authorization.sdk.DeleteUserRequest
import java.util.UUID

fun buildCreateUserRequest(userId: UUID, spaceId: Long, creator: Boolean, jwtToken: String): CreateUserRequest =
    CreateUserRequest.builder()
        .userId(userId.toString())
        .spaceId(spaceId)
        .creator(creator)
        .bearerToken(jwtToken)
        .build()

fun buildDeleteUserRequest(userId: UUID, spaceId: Long, isCreator: Boolean, jwtToken: String): DeleteUserRequest =
    DeleteUserRequest.builder()
        .userId(userId.toString())
        .spaceId(spaceId)
        .isCreator(isCreator)
        .bearerToken(jwtToken)
        .build()

fun buildDeleteSpaceRequest(spaceId: Long, jwtToken: String): DeleteSpaceRequest =
    DeleteSpaceRequest.builder()
        .spaceId(spaceId)
        .bearerToken(jwtToken)
        .build()
