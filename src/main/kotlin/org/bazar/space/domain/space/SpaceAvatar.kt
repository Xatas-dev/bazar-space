package org.bazar.space.domain.space

import java.time.Instant
import java.util.UUID

data class SpaceAvatar(
    val spaceId: Long,
    val fileId: UUID,
    val status: SpaceAvatarStatus,
    val isActive: Boolean,
    val errorReason: String? = null,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now(),
    val url: String? = null,
    val id: Long? = null
) {

    fun uploadSuccess(url: String): SpaceAvatar = copy(
        status = SpaceAvatarStatus.SUCCESS,
        isActive = true,
        url = url,
        updatedAt = Instant.now()
    )

    fun uploadFailed(errorReason: String): SpaceAvatar = copy(
        status = SpaceAvatarStatus.ERROR,
        errorReason = errorReason,
        updatedAt = Instant.now()
    )

    fun setActive(isActive: Boolean): SpaceAvatar = copy(
        isActive = isActive,
        updatedAt = Instant.now()
    )

    companion object {
        fun createAvatarForSpace(spaceId: Long, fileId: UUID) =
            SpaceAvatar(spaceId, fileId, SpaceAvatarStatus.PENDING, isActive = false)
    }
}