package org.bazar.space.infrastructure.event.job

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import org.bazar.space.infrastructure.persistence.repository.outbox.OutboxRepositoryAdapter
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.Duration

@Component
@ConditionalOnProperty(value = ["app.jobs.reset-outbox-locks.enabled"], havingValue = "true")
class ResetOutboxLocksJob(
    private val outboxRepositoryAdapter: OutboxRepositoryAdapter,

    @Value($$"${app.jobs.reset-outbox-locks.max-per-run}")
    private var maxPerRun: Int,
    @Value($$"${app.jobs.reset-outbox-locks.threshold}")
    private var threshold: Duration
) {

    private val log = logger("reset-outbox-locks-job")

    @Scheduled(fixedDelay = 30000)
    fun run() {
        log.debug { "Starting reset outbox locks job" }
        var totalRowsAffected = 0
        do {
            val affectedRows = outboxRepositoryAdapter.resetLock(threshold)
            totalRowsAffected += affectedRows
            log.debug { "Affected $affectedRows, total rows affected $totalRowsAffected" }
        } while (affectedRows > 0 && totalRowsAffected < maxPerRun)
        log.debug { "Ending reset outbox locks job" }
    }


}