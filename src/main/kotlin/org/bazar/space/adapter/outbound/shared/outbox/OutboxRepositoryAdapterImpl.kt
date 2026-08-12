package org.bazar.space.adapter.outbound.shared.outbox

import org.bazar.space.infrastructure.config.app.AppConfigurationProperties
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
    override fun insertAll(entities: List<OutboxEntity>) {
        entities.chunked(batchSize).forEach {
            outboxRepository.insertAll(it)
        }
    }

    @Transactional
    override fun findAndLockNextBatchByEntity(entity: String) =
        outboxRepository.findAndLockNextBatchWhereEntity(entity, batchSize)

    @Transactional
    override fun updateAllSetStatus(entities: List<OutboxEntity>) {
        entities.chunked(batchSize).forEach {
            outboxRepository.updateStatusBatch(it)
        }
    }

    @Transactional
    override fun resetLock(threshold: Duration): Int {
        return outboxRepository.resetLock(threshold, batchSize)
    }

}