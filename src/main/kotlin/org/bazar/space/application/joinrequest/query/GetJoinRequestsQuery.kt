package org.bazar.space.application.joinrequest.query

data class GetJoinRequestsQuery(
    val spaceId: Long,
    val page: Int,
    val size: Int
)
