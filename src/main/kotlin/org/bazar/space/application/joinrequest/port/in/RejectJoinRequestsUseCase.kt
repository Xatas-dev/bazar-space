package org.bazar.space.application.joinrequest.port.`in`

import org.bazar.space.application.joinrequest.command.RejectJoinRequestsCommand

fun interface RejectJoinRequestsUseCase {
    fun execute(command: RejectJoinRequestsCommand): Int
}
