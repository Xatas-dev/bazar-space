package org.bazar.space.util.extension

import org.bazar.space.model.GetSpaceDto
import org.bazar.space.model.GetUsersInSpaceResponse
import org.bazar.space.model.UserInSpaceDto
import org.bazar.space.persistence.entity.Space
import org.bazar.space.util.rest.client.GetRoleNameDto
import org.bazar.space.util.rest.client.UserPersonaDto
import java.util.*

fun Space.toGetSpaceDto() = GetSpaceDto(id!!, name)

fun buildGetUsersInSpaceResponse(
    spaceId: Long,
    userIds: List<UUID>,
    userIdToUserInfoMap: Map<UUID, UserPersonaDto>,
    userIdToRoleNameMap: Map<UUID, GetRoleNameDto>
): GetUsersInSpaceResponse {

    val userList = userIds.map {
        UserInSpaceDto(
            it,
            spaceId,
            userIdToUserInfoMap[it]?.userName ?: "",
            userIdToUserInfoMap[it]?.firstName ?: "",
            userIdToUserInfoMap[it]?.lastName ?: "",
            userIdToRoleNameMap[it]?.name ?: "UNKNOWN"
        )
    }
    return GetUsersInSpaceResponse(userList)
}