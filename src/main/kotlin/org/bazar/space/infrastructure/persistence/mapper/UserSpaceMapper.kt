package org.bazar.space.infrastructure.persistence.mapper

import org.bazar.space.domain.model.UserSpace
import org.bazar.space.infrastructure.persistence.entity.UserSpaceEntity

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
