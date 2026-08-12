package org.bazar.space.infrastructure.util.extension

import java.util.*

fun String.toUuid() = UUID.fromString(this)
