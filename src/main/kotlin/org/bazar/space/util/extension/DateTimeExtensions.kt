package org.bazar.space.util.extension

import java.sql.Timestamp
import java.time.Instant

fun Instant.toTimestamp(): Timestamp = Timestamp.from(this)