package org.bazar.space.application.port.`in`

import org.bazar.space.application.space.query.GetSpacesQuery
import org.bazar.space.domain.space.Space

interface GetSpacesUseCase {
    fun execute(query: GetSpacesQuery): List<Space>
}