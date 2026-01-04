package org.bazar.space.service

import org.bazar.space.entity.Space
import org.bazar.space.model.http.response.GetUserSpacesDtoResponse
import org.bazar.space.repository.SpaceRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class SpaceService(
    private final val spaceRepository: SpaceRepository
) {

    fun createSpace(): Long = spaceRepository.save(Space()).id!!

    fun getAllUserSpaces(userId: UUID): GetUserSpacesDtoResponse {
       val spaceIds = spaceRepository.findAllByUserId(userId)
            .map { it.id!!}
            .toList()
        return GetUserSpacesDtoResponse(spaceIds)
    }

    fun deleteSpaceById(spaceId: Long) = spaceRepository.deleteById(spaceId)

}