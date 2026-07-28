package org.bazar.space.infrastructure.persistence.repository.outbox

import org.bazar.space.infrastructure.persistence.entity.Outbox
import java.time.Duration

interface OutboxRepositoryAdapter {

    fun insertAll(entities: List<Outbox>)

    fun findAndLockNextBatchByEntity(entity: String): List<Outbox>

    fun updateAllSetStatus(entities: List<Outbox>)

    fun resetLock(threshold: Duration): Int

}