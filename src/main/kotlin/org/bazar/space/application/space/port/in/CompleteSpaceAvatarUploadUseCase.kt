package org.bazar.space.application.space.port.`in`

import org.bazar.space.application.space.command.CompleteSpaceAvatarUploadCommand

fun interface CompleteSpaceAvatarUploadUseCase {
    fun execute(command: CompleteSpaceAvatarUploadCommand)
}