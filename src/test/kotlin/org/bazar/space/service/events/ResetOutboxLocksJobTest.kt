package org.bazar.space.service.events

import org.assertj.core.api.Assertions.assertThat
import org.bazar.space.BaseIntegrationTest
import org.bazar.space.model.events.EntityType
import org.bazar.space.persistence.entity.Outbox
import org.bazar.space.persistence.entity.enums.OutboxStatus
import org.bazar.space.persistence.repository.adapter.OutboxRepositoryAdapter
import org.bazar.space.utils.repository.JdbcTestHelper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.TestPropertySource
import java.time.Instant
import kotlin.test.assertTrue

@TestPropertySource(properties = ["app.jobs.space-outbox.enabled=false"])
class ResetOutboxLocksJobTest : BaseIntegrationTest() {

    @Autowired
    private lateinit var resetOutboxLocksJob: ResetOutboxLocksJob
    @Autowired
    private lateinit var outboxRepositoryAdapter: OutboxRepositoryAdapter
    @Autowired
    private lateinit var jdbcTestHelper: JdbcTestHelper

    @Test
    fun testResetOutboxLocksJob_happyPath() {
        //given
        val outboxStuckTimestamp = Instant.now().minusSeconds(120)
        outboxRepositoryAdapter.insertAll(listOf(
            Outbox(
                entity = EntityType.SPACE,
                entityId = 1L,
                payload = "{}",
                status = OutboxStatus.IN_PROGRESS,
                updatedAt = outboxStuckTimestamp,
            )
        ))

        //when
        resetOutboxLocksJob.run()

        //then

        val outboxes = jdbcTestHelper.findAll<Outbox>("outbox")
        assertThat(outboxes).hasSize(1)

        outboxes.first().apply {
            assertEquals(status, OutboxStatus.NEW)
            assertTrue { updatedAt > outboxStuckTimestamp }
        }
    }

    @Test
    fun testResetOutboxLocksJob_shouldNotAffectFreshOutboxes() {
        //given
        outboxRepositoryAdapter.insertAll(listOf(
            Outbox(
                entity = EntityType.SPACE,
                entityId = 1L,
                payload = "{}",
                status = OutboxStatus.IN_PROGRESS
            )
        ))

        //when
        resetOutboxLocksJob.run()

        //then

        val outboxes = jdbcTestHelper.findAll<Outbox>("outbox")
        assertThat(outboxes).hasSize(1)

        outboxes.first().apply {
            assertEquals(status, OutboxStatus.IN_PROGRESS)
        }
    }

}