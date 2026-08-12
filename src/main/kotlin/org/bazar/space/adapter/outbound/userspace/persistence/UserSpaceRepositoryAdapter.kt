package org.bazar.space.adapter.outbound.userspace.persistence

import org.bazar.space.application.userspace.port.out.UserSpaceRepositoryPort
import org.bazar.space.domain.userspace.UserSpace
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class UserSpaceRepositoryAdapter(
    private val userSpaceEntityRepository: UserSpaceEntityRepository
) : UserSpaceRepositoryPort {

    override fun save(userSpace: UserSpace): UserSpace =
        userSpaceEntityRepository.save(userSpace.toEntity()).toDomain()

    override fun findAllBySpaceId(spaceId: Long): List<UserSpace> =
        userSpaceEntityRepository.findAllBySpaceId(spaceId).map { it.toDomain() }

    override fun deleteAllBySpaceId(spaceId: Long) {
        userSpaceEntityRepository.deleteAllBySpaceId(spaceId)
    }

    override fun deleteBySpaceIdAndUserId(spaceId: Long, userId: UUID) {
        userSpaceEntityRepository.deleteBySpaceIdAndUserId(spaceId, userId)
    }

    override fun findBySpaceIdAndUserId(spaceId: Long, userId: UUID): UserSpace? =
        userSpaceEntityRepository.findBySpaceIdAndUserId(spaceId, userId).orElse(null)?.toDomain()
}
