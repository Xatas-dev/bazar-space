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

    override fun deleteBySpaceIdAndUserId(spaceId: Long, userId: UUID) {
        spaceJoinRequestEntityRepository.deleteBySpaceIdAndUserId(spaceId, userId)
    }
}
