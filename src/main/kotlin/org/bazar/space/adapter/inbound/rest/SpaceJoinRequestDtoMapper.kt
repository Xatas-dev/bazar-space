package org.bazar.space.adapter.inbound.rest

import org.bazar.space.application.joinrequest.JoinRequestWithUser
import org.bazar.space.application.joinrequest.command.ApproveJoinRequestsCommand
import org.bazar.space.application.joinrequest.query.GetJoinRequestsQuery
import org.bazar.space.application.shared.PageResult
import org.bazar.space.domain.exception.InvalidRequestException
import org.bazar.space.domain.joinrequest.SpaceJoinRequest
import org.bazar.space.model.ApproveJoinRequestsRequest
import org.bazar.space.model.JoinRequestDto
import org.bazar.space.model.JoinRequestUserDto
import org.bazar.space.model.JoinRequestsPageResponse
import org.bazar.space.model.SpaceJoinRequestResponse

fun SpaceJoinRequest.toSpaceJoinRequestResponse() = SpaceJoinRequestResponse(
    createdAt = createdAt
)

fun ApproveJoinRequestsRequest.toCommand(spaceId: Long): ApproveJoinRequestsCommand {
    val ids = requestIds
    val all = all
    return when {
        ids != null && all != null ->
            throw InvalidRequestException("only one of requestIds / all must be passed, got both")
        ids != null -> {
            ApproveJoinRequestsCommand.Selected(spaceId, ids.toSet())
        }
        all == true -> ApproveJoinRequestsCommand.All(spaceId)
        all == false -> throw InvalidRequestException("all can only be true, got false")
        else -> throw InvalidRequestException("one of requestIds / all must be passed, got none")
    }
}

fun getJoinRequestsQuery(spaceId: Long, page: Int, size: Int): GetJoinRequestsQuery {
    if (page < 0) throw InvalidRequestException("page must not be negative, got $page")
    if (size < 1) throw InvalidRequestException("size must be positive, got $size")
    return GetJoinRequestsQuery(spaceId, page, size)
}

fun PageResult<JoinRequestWithUser>.toJoinRequestsPageResponse() = JoinRequestsPageResponse(
    items = items.map { it.toJoinRequestDto() },
    page = page,
    propertySize = size,
    total = total
)

private fun JoinRequestWithUser.toJoinRequestDto() = JoinRequestDto(
    id = id,
    user = JoinRequestUserDto(
        userId = user.userId,
        username = user.username,
        displayName = user.displayName,
        avatarUrl = user.avatarUrl
    ),
    createdAt = createdAt
)
