package org.bazar.space.utils

import org.bazar.space.adapter.outbound.space.persistence.SpaceAvatarEntity
import org.bazar.space.adapter.outbound.space.persistence.SpaceAvatarEntityRepository
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.UUID

@Component
class SpaceAvatarCreator(
    private val spaceAvatarEntityRepository: SpaceAvatarEntityRepository
) {
    fun create(
        spaceId: Long,
        fileId: UUID = UUID.randomUUID(),
        isActive: Boolean = true,
        status: String = "SUCCESS",
        url: String = "https://s3.test/$fileId",
        errorReason: String? = null,
        createdAt: Instant = Instant.now(),
        updatedAt: Instant = createdAt,
    ): SpaceAvatarEntity = spaceAvatarEntityRepository.save(
        SpaceAvatarEntity(
            spaceId = spaceId,
            fileId = fileId,
            isActive = isActive,
            status = status,
            url = url,
            errorReason = errorReason,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
    )

}
