package org.bazar.space.infrastructure.util.extension

import java.net.URI
import java.util.*

fun String.toUuid() = UUID.fromString(this)

fun String.toUri() = URI.create(this)