package org.bazar.space.application.joinrequest.command

sealed interface ApproveJoinRequestsCommand {
    val spaceId: Long

    data class Selected(override val spaceId: Long, val requestIds: Set<Long>) : ApproveJoinRequestsCommand

    data class All(override val spaceId: Long) : ApproveJoinRequestsCommand
}
