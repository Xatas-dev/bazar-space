package org.bazar.space.api.controller

import org.bazar.space.domain.model.Space
import org.bazar.space.domain.model.UserInSpace
import org.bazar.space.model.GetSpaceDto
import org.bazar.space.model.GetSpacesResponse
import org.bazar.space.model.GetUsersInSpaceResponse
import org.bazar.space.model.SimpleRoleDto
import org.bazar.space.model.UserInSpaceDto

fun Space.toGetSpaceDto() = GetSpaceDto(id!!, name)

fun List<Space>.toGetSpacesResponse() = GetSpacesResponse(map { it.toGetSpaceDto() }.toList())

fun UserInSpace.toUserInSpaceDto() = UserInSpaceDto(
    userId = userId,
    spaceId = spaceId,
    userName = userName,
    firstName = firstName,
    lastName = lastName,
    role = role?.let { SimpleRoleDto(it.id, it.name, it.isVisible) }
)

fun List<UserInSpace>.toGetUsersInSpaceResponse() = GetUsersInSpaceResponse(map { it.toUserInSpaceDto() })
