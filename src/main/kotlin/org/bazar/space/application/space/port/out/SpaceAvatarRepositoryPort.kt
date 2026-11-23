package org.bazar.space.application.space.port.out

import org.bazar.space.domain.space.SpaceAvatar
import java.util.UUID

interface SpaceAvatarRepositoryPort {
    fun insert(avatar: SpaceAvatar): SpaceAvatar
    fun update(avatar: SpaceAvatar): SpaceAvatar
    fun findActiveBySpaceId(spaceId: Long): SpaceAvatar?
    fun findActiveBySpaceIds(spaceIds: Collection<Long>): List<SpaceAvatar>
    fun findByFileId(fileId: UUID): SpaceAvatar?
    fun findAllBySpaceId(spaceId: Long): List<SpaceAvatar>
    fun deleteAllBySpaceId(spaceId: Long)
}