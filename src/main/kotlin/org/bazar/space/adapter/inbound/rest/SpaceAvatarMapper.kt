package org.bazar.space.adapter.inbound.rest

import org.bazar.space.domain.space.SpaceAvatar
import org.bazar.space.model.GetSpaceAvatarsResponse
import org.bazar.space.model.SpaceAvatarDto

fun SpaceAvatar.toSpaceAvatarDto(): SpaceAvatarDto = SpaceAvatarDto(
    id = requireNotNull(id) { "Avatar id must not be null" },
    status = status.name,
    url = url,
    createdAt = createdAt
)

fun List<SpaceAvatar>.toGetSpaceAvatarsResponse() = GetSpaceAvatarsResponse(
    avatars = map { it.toSpaceAvatarDto() }
)