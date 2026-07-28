package org.bazar.space.persistence.repository.adapter

import org.bazar.space.model.events.EntityType
import org.bazar.space.persistence.entity.Outbox
import java.time.Duration

interface OutboxRepositoryAdapter {

    fun insertAll(entities: List<Outbox>)

    fun findAndLockNextBatchByEntity(entity: EntityType): List<Outbox>

    fun updateAllSetStatus(entities: List<Outbox>)

    fun resetLock(threshold: Duration): Int

}