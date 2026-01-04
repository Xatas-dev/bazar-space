package org.bazar.space.util.extension

import java.util.*

fun String.toUuid() = UUID.fromString(this)
