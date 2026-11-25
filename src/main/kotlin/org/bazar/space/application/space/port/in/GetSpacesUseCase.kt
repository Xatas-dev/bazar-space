package org.bazar.space.application.port.`in`

import org.bazar.space.domain.space.Space

interface GetSpacesUseCase {
    fun execute(): List<Space>
}