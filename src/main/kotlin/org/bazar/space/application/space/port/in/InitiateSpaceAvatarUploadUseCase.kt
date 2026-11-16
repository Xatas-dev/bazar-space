package org.bazar.space.application.space.port.`in`

import org.bazar.space.application.space.command.InitiateSpaceAvatarUploadCommand

fun interface InitiateSpaceAvatarUploadUseCase {
    fun execute(command: InitiateSpaceAvatarUploadCommand): InitiateAvatarUploadResult
}