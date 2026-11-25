package org.bazar.space.utils

import org.bazar.space.adapter.outbound.joinrequest.persistence.SpaceJoinRequestEntity
import org.bazar.space.adapter.outbound.joinrequest.persistence.SpaceJoinRequestEntityRepository
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

@Component
class SpaceJoinRequestCreator(
    private val spaceJoinRequestEntityRepository: SpaceJoinRequestEntityRepository
) {
    fun create(
        spaceId: Long,
        userId: UUID = UUID.randomUUID(),
        createdAt: Instant = Instant.now(),
        updatedAt: Instant = createdAt,
    ): SpaceJoinRequestEntity = spaceJoinRequestEntityRepository.save(
        SpaceJoinRequestEntity(
            spaceId = spaceId,
            userId = userId,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
    )

    fun createMany(spaceId: Long, count: Int): List<SpaceJoinRequestEntity> =
        spaceJoinRequestEntityRepository.saveAll(
            List(count) { SpaceJoinRequestEntity(spaceId = spaceId, userId = UUID.randomUUID()) }
        ).toList()
}
