package org.bazar.space.application.joinrequest.port.`in`

import org.bazar.space.application.joinrequest.command.ApproveJoinRequestsCommand

fun interface ApproveJoinRequestsUseCase {
    fun execute(command: ApproveJoinRequestsCommand): Int
}
