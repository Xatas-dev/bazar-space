package org.bazar.space.adapter.inbound.rest

import org.bazar.space.domain.joinrequest.SpaceJoinRequest
import org.bazar.space.model.SpaceJoinRequestResponse

fun SpaceJoinRequest.toSpaceJoinRequestResponse() = SpaceJoinRequestResponse(
    id = requireNotNull(id) { "Join request id must not be null" },
    createdAt = createdAt
)
