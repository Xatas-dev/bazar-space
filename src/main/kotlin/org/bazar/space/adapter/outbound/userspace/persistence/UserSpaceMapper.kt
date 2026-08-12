package org.bazar.space.adapter.outbound.userspace.persistence

import org.bazar.space.domain.userspace.UserSpace

fun UserSpaceEntity.toDomain(): UserSpace = UserSpace(
    id = id,
    spaceId = spaceId,
    userId = userId,
    creator = creator,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun UserSpace.toEntity(): UserSpaceEntity = UserSpaceEntity(
    id = id,
    spaceId = spaceId,
    userId = userId,
    creator = creator,
    createdAt = createdAt,
    updatedAt = updatedAt
)
