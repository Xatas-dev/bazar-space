package org.bazar.space.adapter.outbound.space.persistence

import org.bazar.space.domain.space.SpaceAvatar
import org.bazar.space.domain.space.SpaceAvatarStatus

fun SpaceAvatar.toEntity(): SpaceAvatarEntity = SpaceAvatarEntity(
    spaceId = spaceId,
    fileId = fileId,
    isActive = isActive,
    status = status.name,
    url = url,
    errorReason = errorReason,
    createdAt = createdAt,
    updatedAt = updatedAt,
    id = id
)

fun SpaceAvatarEntity.toDomain(): SpaceAvatar = SpaceAvatar(
    spaceId = spaceId,
    fileId = fileId,
    status = SpaceAvatarStatus.valueOf(status),
    isActive = isActive,
    errorReason = errorReason,
    createdAt = createdAt,
    updatedAt = updatedAt,
    url = url,
    id = id
)