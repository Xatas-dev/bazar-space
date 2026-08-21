package org.bazar.space.adapter.inbound.rest

import org.bazar.space.domain.space.Space
import org.bazar.space.application.userspace.UserInSpace
import org.bazar.space.application.userspace.UserInSpaceRaw
import org.bazar.space.model.GetSpaceDto
import org.bazar.space.model.GetSpacesResponse
import org.bazar.space.model.GetUsersInSpaceResponse
import org.bazar.space.model.SimpleRoleDto
import org.bazar.space.model.UserInSpaceDto
import org.bazar.space.model.UserInSpaceRawDto

fun Space.toGetSpaceDto() = GetSpaceDto(requireNotNull(id) { "Space id must not be null" }, name)

fun List<Space>.toGetSpacesResponse() = GetSpacesResponse(map { it.toGetSpaceDto() }.toList())

fun UserInSpace.toUserInSpaceDto() = UserInSpaceDto(
    userId = userId,
    spaceId = spaceId,
    userName = userName,
    firstName = firstName,
    lastName = lastName,
    creator = creator,
    role = role?.let { SimpleRoleDto(it.id, it.name, it.isVisible) }
)

fun UserInSpaceRaw.toUserInSpaceRawDto() = UserInSpaceRawDto(
    userId = userId,
    spaceId = spaceId,
    creator = creator
)

fun List<UserInSpace>.toGetUsersInSpaceResponse() = GetUsersInSpaceResponse(map { it.toUserInSpaceDto() })
