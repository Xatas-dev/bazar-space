package org.bazar.space.adapter.outbound.joinrequest.persistence

import org.bazar.space.application.joinrequest.port.out.SpaceJoinRequestRepositoryPort
import org.bazar.space.application.shared.PageResult
import org.bazar.space.domain.joinrequest.SpaceJoinRequest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
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

    override fun findPageBySpaceId(spaceId: Long, page: Int, size: Int): PageResult<SpaceJoinRequest> {
        val pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdAt"), Sort.Order.desc("id")))
        val result = spaceJoinRequestEntityRepository.findAllBySpaceId(spaceId, pageable)
        return PageResult(
            items = result.content.map { it.toDomain() },
            page = result.number,
            size = result.size,
            total = result.totalElements
        )
    }

    override fun findAllBySpaceIdAndIds(spaceId: Long, requestIds: Collection<Long>): List<SpaceJoinRequest> =
        if (requestIds.isEmpty()) emptyList()
        else spaceJoinRequestEntityRepository.findAllBySpaceIdAndIdIn(spaceId, requestIds).map { it.toDomain() }

    override fun deleteBySpaceIdAndIds(spaceId: Long, requestIds: Collection<Long>): Int =
        if (requestIds.isEmpty()) 0 else spaceJoinRequestEntityRepository.deleteBySpaceIdAndIds(spaceId, requestIds)

    override fun deleteBatchBySpaceId(spaceId: Long, limit: Int): Int =
        spaceJoinRequestEntityRepository.deleteBatchBySpaceId(spaceId, limit)

    override fun deleteBySpaceIdAndUserId(spaceId: Long, userId: UUID) {
        spaceJoinRequestEntityRepository.deleteBySpaceIdAndUserId(spaceId, userId)
    }
}
