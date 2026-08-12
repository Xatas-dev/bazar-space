package org.bazar.space.application.port.`in`

import org.bazar.space.application.userspace.command.DeleteUserFromSpaceCommand

interface DeleteUserFromSpaceUseCase {
    fun execute(command: DeleteUserFromSpaceCommand)
}