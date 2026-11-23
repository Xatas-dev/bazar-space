package org.bazar.space.adapter.outbound.space.persistence

import org.bazar.space.application.space.port.out.SpaceAvatarRepositoryPort
import org.bazar.space.domain.space.SpaceAvatar
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class SpaceAvatarRepositoryAdapter(
    private val spaceAvatarEntityRepository: SpaceAvatarEntityRepository
) : SpaceAvatarRepositoryPort {

    override fun insert(avatar: SpaceAvatar): SpaceAvatar =
        spaceAvatarEntityRepository.save(avatar.toEntity()).toDomain()

    override fun update(avatar: SpaceAvatar): SpaceAvatar =
        spaceAvatarEntityRepository.save(avatar.toEntity()).toDomain()

    override fun findActiveBySpaceId(spaceId: Long): SpaceAvatar? =
        spaceAvatarEntityRepository.findBySpaceIdAndIsActive(spaceId, true)?.toDomain()

    override fun findActiveBySpaceIds(spaceIds: Collection<Long>): List<SpaceAvatar> =
        spaceAvatarEntityRepository.findBySpaceIdInAndIsActive(spaceIds, true).map { it.toDomain() }

    override fun findByFileId(fileId: UUID): SpaceAvatar? =
        spaceAvatarEntityRepository.findByFileId(fileId)?.toDomain()

    override fun findAllBySpaceId(spaceId: Long): List<SpaceAvatar> =
        spaceAvatarEntityRepository.findBySpaceIdOrderByCreatedAtDesc(spaceId).map { it.toDomain() }

    override fun deleteAllBySpaceId(spaceId: Long) {
        spaceAvatarEntityRepository.deleteAllBySpaceId(spaceId)
    }
}