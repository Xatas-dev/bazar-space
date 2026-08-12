package org.bazar.space.application.userspace.port.`in`

import org.bazar.space.application.userspace.command.AddUserToSpaceCommand

interface AddUserToSpaceUseCase {
    fun execute(command: AddUserToSpaceCommand)
}