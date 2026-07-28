package org.bazar.space.infrastructure.persistence.repository

import org.bazar.space.domain.model.Space
import org.bazar.space.domain.port.SpaceRepositoryPort
import org.bazar.space.infrastructure.persistence.mapper.toDomain
import org.bazar.space.infrastructure.persistence.mapper.toEntity
import org.bazar.space.infrastructure.persistence.repository.SpaceEntityRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class SpaceRepositoryAdapter(
    private val spaceEntityRepository: SpaceEntityRepository
) : SpaceRepositoryPort {

    override fun save(space: Space): Space =
        spaceEntityRepository.save(space.toEntity()).toDomain()

    override fun findById(id: Long): Space? =
        spaceEntityRepository.findById(id).orElse(null)?.toDomain()

    override fun deleteById(id: Long) =
        spaceEntityRepository.deleteById(id)

    override fun findAllByUserId(userId: UUID): List<Space> =
        spaceEntityRepository.findAllByUserId(userId).map { it.toDomain() }
}
