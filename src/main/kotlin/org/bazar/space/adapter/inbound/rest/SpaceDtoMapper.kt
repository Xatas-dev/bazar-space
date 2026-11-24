package org.bazar.space.adapter.inbound.rest

import org.bazar.space.domain.space.Space
import org.bazar.space.application.userspace.UserInSpace
import org.bazar.space.application.userspace.UserInSpaceRaw
import org.bazar.space.infrastructure.util.extension.toUri
import org.bazar.space.model.GetSpaceDto
import org.bazar.space.model.GetSpacesResponse
import org.bazar.space.model.GetUsersInSpaceResponse
import org.bazar.space.model.SpaceJoinLinkResponse
import org.bazar.space.model.SimpleRoleDto
import org.bazar.space.model.UserInSpaceDto
import org.bazar.space.model.UserInSpaceRawDto

fun Space.toGetSpaceDto(avatarUrl: String? = null) = GetSpaceDto(
    id = requireNotNull(id) { "Space id must not be null" },
    name = name,
    avatarUrl = avatarUrl
)

fun List<Space>.toGetSpacesResponse(avatarUrlsBySpaceId: Map<Long, String?>) =
    GetSpacesResponse(map { it.toGetSpaceDto(avatarUrlsBySpaceId[it.id]) }.toList())

fun String.toSpaceJoinLinkResponse(baseUrl: String) =
    SpaceJoinLinkResponse(url = "${baseUrl}/$this")

fun UserInSpace.toUserInSpaceDto() = UserInSpaceDto(
    userId = userId,
    spaceId = spaceId,
    username = username,
    displayName = displayName,
    avatarUrl = avatarUrl?.toUri(),
    creator = creator,
    role = role?.let { SimpleRoleDto(it.id, it.name, it.isVisible) }
)

fun UserInSpaceRaw.toUserInSpaceRawDto() = UserInSpaceRawDto(
    userId = userId,
    spaceId = spaceId,
    creator = creator
)

fun List<UserInSpace>.toGetUsersInSpaceResponse() = GetUsersInSpaceResponse(map { it.toUserInSpaceDto() })
