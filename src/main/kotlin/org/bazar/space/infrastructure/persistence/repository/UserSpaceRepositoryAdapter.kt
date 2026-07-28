package org.bazar.space.infrastructure.persistence.repository

import org.bazar.space.domain.model.UserSpace
import org.bazar.space.domain.port.UserSpaceRepositoryPort
import org.bazar.space.infrastructure.persistence.mapper.toDomain
import org.bazar.space.infrastructure.persistence.mapper.toEntity
import org.bazar.space.infrastructure.persistence.repository.UserSpaceEntityRepository
import org.springframework.stereotype.Repository
import java.util.UUID

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
