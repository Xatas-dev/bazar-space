package org.bazar.space.application.userspace.port.`in`.impl

import org.bazar.space.application.userspace.port.`in`.GetUserInSpaceUseCase
import org.bazar.space.application.userspace.query.GetUserInSpaceQuery
import org.bazar.space.application.userspace.port.out.RoleInfoProvider
import org.bazar.space.application.userspace.port.out.UserInfoProvider
import org.bazar.space.application.userspace.port.out.UserSpaceRepositoryPort
import org.bazar.space.domain.exception.DomainException
import org.bazar.space.domain.exception.DomainErrors
import org.bazar.space.domain.space.SpaceMembership
import org.bazar.space.application.userspace.RoleInfo
import org.bazar.space.application.userspace.UserInSpace
import org.springframework.stereotype.Service

@Service
class GetUserInSpaceUseCaseImpl(
    private val userSpaceRepositoryPort: UserSpaceRepositoryPort,
    private val userInfoProvider: UserInfoProvider,
    private val roleInfoProvider: RoleInfoProvider
) : GetUserInSpaceUseCase {

    override fun execute(query: GetUserInSpaceQuery): UserInSpace {
        val members = userSpaceRepositoryPort.findAllBySpaceId(query.spaceId)
        SpaceMembership.ensureUserIsMember(members, query.authenticatedUserId)

        val userSpace = userSpaceRepositoryPort.findBySpaceIdAndUserId(query.spaceId, query.userId)
            ?: throw DomainException(DomainErrors.USER_NOT_FOUND, query.userId, query.spaceId)

        val persona = userInfoProvider.getUsersByIds(listOf(query.userId))[query.userId]
        val role = roleInfoProvider.getRoleNames(query.spaceId, listOf(query.userId))[query.userId]

        return UserInSpace(
            userId = query.userId,
            spaceId = query.spaceId,
            userName = persona?.userName ?: "",
            firstName = persona?.firstName ?: "",
            lastName = persona?.lastName ?: "",
            creator = userSpace.creator,
            role = role?.let { RoleInfo(it.id, it.name, it.isVisible) }
        )
    }
}
