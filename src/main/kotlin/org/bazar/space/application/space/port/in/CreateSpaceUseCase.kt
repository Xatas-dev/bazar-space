package org.bazar.space.application.port.`in`

import org.bazar.space.application.space.command.CreateSpaceCommand
import org.bazar.space.domain.space.Space

interface CreateSpaceUseCase {
    fun execute(command: CreateSpaceCommand): Space
}