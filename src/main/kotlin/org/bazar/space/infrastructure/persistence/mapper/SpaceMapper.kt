package org.bazar.space.infrastructure.persistence.mapper

import org.bazar.space.domain.model.Space
import org.bazar.space.infrastructure.persistence.entity.SpaceEntity

fun SpaceEntity.toDomain(): Space = Space(
    id = id,
    name = name,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun Space.toEntity(): SpaceEntity = SpaceEntity(
    id = id,
    name = name,
    createdAt = createdAt,
    updatedAt = updatedAt
)
