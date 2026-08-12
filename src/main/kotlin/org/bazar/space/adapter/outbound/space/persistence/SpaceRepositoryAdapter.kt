package org.bazar.space.adapter.outbound.space.persistence

import org.bazar.space.application.space.port.out.SpaceRepositoryPort
import org.bazar.space.domain.space.Space
import org.springframework.stereotype.Repository
import java.util.*

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
