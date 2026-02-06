package org.bazar.space.util.extension

import org.bazar.space.persistence.entity.Space
import org.bazar.space.model.GetSpaceDto

fun Space.toGetSpaceDto() = GetSpaceDto(id!!, name)