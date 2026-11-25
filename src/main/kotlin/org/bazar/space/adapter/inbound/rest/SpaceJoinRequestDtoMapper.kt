package org.bazar.space.adapter.inbound.rest

import org.bazar.space.application.joinrequest.command.ApproveJoinRequestsCommand
import org.bazar.space.domain.exception.InvalidRequestException
import org.bazar.space.domain.joinrequest.SpaceJoinRequest
import org.bazar.space.model.ApproveJoinRequestsRequest
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
