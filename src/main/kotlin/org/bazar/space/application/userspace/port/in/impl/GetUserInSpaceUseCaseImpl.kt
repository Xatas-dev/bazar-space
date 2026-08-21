package org.bazar.space.application.userspace.port.`in`.impl

import org.bazar.space.application.userspace.UserInSpaceRaw
import org.bazar.space.application.userspace.port.`in`.GetRawUserInSpaceUseCase
import org.bazar.space.application.userspace.port.out.UserSpaceRepositoryPort
import org.bazar.space.application.userspace.query.GetUserInSpaceQuery
import org.bazar.space.domain.exception.DomainErrors
import org.bazar.space.domain.exception.DomainException
import org.bazar.space.domain.space.SpaceMembership
import org.springframework.stereotype.Service

@Service
class GetUserInSpaceUseCaseImpl(
    private val userSpaceRepositoryPort: UserSpaceRepositoryPort,
) : GetRawUserInSpaceUseCase {

    override fun execute(query: GetUserInSpaceQuery): UserInSpaceRaw {
        val members = userSpaceRepositoryPort.findAllBySpaceId(query.spaceId)
        SpaceMembership.ensureUserIsMember(members, query.authenticatedUserId)

        val userSpace = userSpaceRepositoryPort.findBySpaceIdAndUserId(query.spaceId, query.userId)
            ?: throw DomainException(DomainErrors.USER_NOT_FOUND, query.userId, query.spaceId)

        return UserInSpaceRaw(
            userId = query.userId,
            spaceId = query.spaceId,
            creator = userSpace.creator
        )
    }
}
