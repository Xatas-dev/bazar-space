package org.bazar.space.service.events

import org.bazar.space.persistence.repository.adapter.OutboxRepositoryAdapter
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.Duration

@Component
@ConditionalOnProperty(value = ["app.jobs.reset-outbox-locks.enabled"], havingValue = "true")
class ResetOutboxLocksJob(
    private val outboxRepositoryAdapter: OutboxRepositoryAdapter
) {

    @Value($$"${app.jobs.reset-outbox-locks.threshold}")
    private lateinit var threshold: Duration

    @Scheduled(fixedDelay = 30000)
    fun run() {

        do {
            val affectedRows = outboxRepositoryAdapter.resetLock(threshold)
        } while (affectedRows > 0)

    }


}