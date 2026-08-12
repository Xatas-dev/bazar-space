package org.bazar.space.adapter.outbound.shared.outbox

import java.time.Duration

interface OutboxRepositoryAdapter {

    fun insertAll(entities: List<OutboxEntity>)

    fun findAndLockNextBatchByEntity(entity: String): List<OutboxEntity>

    fun updateAllSetStatus(entities: List<OutboxEntity>)

    fun resetLock(threshold: Duration): Int

}