package org.bazar.space.application.joinrequest.port.`in`

import org.bazar.space.application.joinrequest.command.CreateJoinRequestCommand
import org.bazar.space.domain.joinrequest.SpaceJoinRequest

fun interface CreateJoinRequestUseCase {
    fun execute(command: CreateJoinRequestCommand): SpaceJoinRequest
}
