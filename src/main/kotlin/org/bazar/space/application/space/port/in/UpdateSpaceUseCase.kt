package org.bazar.space.application.port.`in`

import org.bazar.space.application.space.command.UpdateSpaceCommand
import org.bazar.space.domain.space.Space

interface UpdateSpaceUseCase {
    fun execute(command: UpdateSpaceCommand): Space
}