package org.bazar.space.service

import org.bazar.space.persistence.entity.Space
import org.bazar.space.model.GetSpaceDto
import org.bazar.space.model.GetSpacesResponse
import org.bazar.space.persistence.repository.SpaceRepository
import org.bazar.space.util.exceptions.ApiException
import org.bazar.space.util.exceptions.ApiExceptions
import org.bazar.space.util.extension.toGetSpaceDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class SpaceService(
    private final val spaceRepository: SpaceRepository
) {

    fun createSpace(name: String): GetSpaceDto = spaceRepository.save(Space(name = name))
        .toGetSpaceDto()

    fun getAllUserSpaces(userId: UUID): GetSpacesResponse {
        val spaces = spaceRepository.findAllByUserId(userId)
            .map {
                GetSpaceDto(it.id!!, it.name)
            }
            .toList()
        return GetSpacesResponse(spaces)
    }

    fun deleteSpaceById(spaceId: Long) = spaceRepository.deleteById(spaceId)

    fun patchSpace(spaceId: Long, name: String): GetSpaceDto {
        val space = spaceRepository.findById(spaceId)
            .orElseThrow { ApiException(ApiExceptions.SPACE_NOT_FOUND, spaceId) }
        space.name = name
        spaceRepository.save(space)
        return GetSpaceDto(spaceId, name)
    }

}