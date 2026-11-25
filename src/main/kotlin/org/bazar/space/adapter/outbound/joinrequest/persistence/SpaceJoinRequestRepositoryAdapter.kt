package org.bazar.space.adapter.outbound.joinrequest.persistence

import org.bazar.space.application.joinrequest.port.out.SpaceJoinRequestRepositoryPort
import org.bazar.space.domain.joinrequest.SpaceJoinRequest
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class SpaceJoinRequestRepositoryAdapter(
    private val spaceJoinRequestEntityRepository: SpaceJoinRequestEntityRepository
) : SpaceJoinRequestRepositoryPort {

    override fun save(request: SpaceJoinRequest): SpaceJoinRequest =
        spaceJoinRequestEntityRepository.save(request.toEntity()).toDomain()

    override fun findAllBySpaceId(spaceId: Long): List<SpaceJoinRequest> =
        spaceJoinRequestEntityRepository.findAllBySpaceIdOrderByCreatedAt(spaceId).map { it.toDomain() }

    override fun findBySpaceId(spaceId: Long, limit: Int): List<SpaceJoinRequest> =
        spaceJoinRequestEntityRepository.findBySpaceId(spaceId, limit).map { it.toDomain() }

    override fun findAllBySpaceIdAndIds(spaceId: Long, requestIds: Collection<Long>): List<SpaceJoinRequest> =
        if (requestIds.isEmpty()) emptyList()
        else spaceJoinRequestEntityRepository.findAllBySpaceIdAndIdIn(spaceId, requestIds).map { it.toDomain() }

    override fun deleteBySpaceIdAndIds(spaceId: Long, requestIds: Collection<Long>): Int =
        if (requestIds.isEmpty()) 0 else spaceJoinRequestEntityRepository.deleteBySpaceIdAndIds(spaceId, requestIds)

    override fun deleteBySpaceIdAndUserId(spaceId: Long, userId: UUID) {
        spaceJoinRequestEntityRepository.deleteBySpaceIdAndUserId(spaceId, userId)
    }
}
