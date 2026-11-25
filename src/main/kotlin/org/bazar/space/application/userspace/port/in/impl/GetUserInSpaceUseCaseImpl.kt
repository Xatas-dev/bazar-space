package org.bazar.space.application.userspace.port.`in`.impl

import org.bazar.space.application.shared.port.out.CurrentUserProvider
import org.bazar.space.application.userspace.UserInSpaceRaw
import org.bazar.space.application.userspace.port.`in`.GetRawUserInSpaceUseCase
import org.bazar.space.application.userspace.port.out.UserSpaceRepositoryPort
import org.bazar.space.application.userspace.query.GetUserInSpaceQuery
import org.bazar.space.domain.exception.UserNotFoundException
import org.bazar.space.domain.space.SpaceMembership
import org.springframework.stereotype.Service

@Service
class GetUserInSpaceUseCaseImpl(
    private val userSpaceRepositoryPort: UserSpaceRepositoryPort,
    private val currentUser: CurrentUserProvider
) : GetRawUserInSpaceUseCase {

    override fun execute(query: GetUserInSpaceQuery): UserInSpaceRaw {
        val members = userSpaceRepositoryPort.findAllBySpaceId(query.spaceId)
        SpaceMembership.ensureUserIsMember(members, currentUser.id)

        val userSpace = userSpaceRepositoryPort.findBySpaceIdAndUserId(query.spaceId, query.userId)
            ?: throw UserNotFoundException(query.userId, query.spaceId)

        return UserInSpaceRaw(
            userId = query.userId,
            spaceId = query.spaceId,
            creator = userSpace.creator
        )
    }
}
