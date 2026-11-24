package org.bazar.space.adapter.outbound.joinrequest.persistence

import org.bazar.space.domain.joinrequest.SpaceJoinRequest

fun SpaceJoinRequestEntity.toDomain(): SpaceJoinRequest = SpaceJoinRequest(
    id = id,
    spaceId = spaceId,
    userId = userId,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun SpaceJoinRequest.toEntity(): SpaceJoinRequestEntity = SpaceJoinRequestEntity(
    id = id,
    spaceId = spaceId,
    userId = userId,
    createdAt = createdAt,
    updatedAt = updatedAt
)
