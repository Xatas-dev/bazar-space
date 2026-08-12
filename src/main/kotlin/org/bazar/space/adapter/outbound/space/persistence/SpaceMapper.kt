package org.bazar.space.adapter.outbound.space.persistence

import org.bazar.space.domain.space.Space

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
