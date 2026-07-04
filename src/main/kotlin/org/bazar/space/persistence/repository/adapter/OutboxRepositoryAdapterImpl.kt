package org.bazar.space.persistence.repository.adapter

import org.bazar.space.config.app.AppConfigurationProperties
import org.bazar.space.model.events.EntityType
import org.bazar.space.persistence.entity.Outbox
import org.bazar.space.persistence.repository.OutboxRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.Duration

@Repository
class OutboxRepositoryAdapterImpl(
    private val appConfigurationProperties: AppConfigurationProperties,
    private val outboxRepository: OutboxRepository
) : OutboxRepositoryAdapter {

    private val batchSize by lazy {
        appConfigurationProperties.database.batchSize
    }

    @Transactional
    override fun insertAll(entities: List<Outbox>) {
        entities.chunked(batchSize).forEach {
            outboxRepository.insertAll(it)
        }
    }

    @Transactional
    override fun findAndLockNextBatchByEntity(entity: EntityType) =
        outboxRepository.findAndLockNextBatchWhereEntity(entity.name, batchSize)

    @Transactional
    override fun updateAllSetStatus(entities: List<Outbox>) {
        entities.chunked(batchSize).forEach {
            outboxRepository.updateStatusBatch(it)
        }
    }

    override fun resetLock(threshold: Duration): Int {
        return outboxRepository.resetLock(threshold, batchSize)
    }

}