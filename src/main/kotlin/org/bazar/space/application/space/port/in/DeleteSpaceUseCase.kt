package org.bazar.space.application.port.`in`

import org.bazar.space.application.space.command.DeleteSpaceCommand

interface DeleteSpaceUseCase {
    fun execute(command: DeleteSpaceCommand)
}