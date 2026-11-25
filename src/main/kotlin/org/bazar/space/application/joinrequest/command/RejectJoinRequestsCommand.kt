package org.bazar.space.application.joinrequest.command

sealed interface RejectJoinRequestsCommand {
    val spaceId: Long

    data class Selected(override val spaceId: Long, val requestIds: Set<Long>) : RejectJoinRequestsCommand

    data class All(override val spaceId: Long) : RejectJoinRequestsCommand
}
