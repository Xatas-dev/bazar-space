package org.bazar.space.application.userspace.port.`in`.impl

import org.bazar.space.application.port.`in`.GetUsersInSpaceUseCase
import org.bazar.space.application.userspace.query.GetUsersInSpaceQuery
import org.bazar.space.domain.space.SpaceMembership
import org.bazar.space.domain.userspace.RoleInfo
import org.bazar.space.domain.userspace.UserInSpace
import org.bazar.space.application.userspace.port.out.RoleInfoProvider
import org.bazar.space.application.userspace.port.out.UserInfoProvider
import org.bazar.space.application.userspace.port.out.UserSpaceRepositoryPort
import org.springframework.stereotype.Service

@Service
class GetUsersInSpaceUseCaseImpl(
    private val userSpaceRepositoryPort: UserSpaceRepositoryPort,
    private val userInfoProvider: UserInfoProvider,
    private val roleInfoProvider: RoleInfoProvider
) : GetUsersInSpaceUseCase {

    override fun execute(query: GetUsersInSpaceQuery): List<UserInSpace> {
        val members = userSpaceRepositoryPort.findAllBySpaceId(query.spaceId)
        SpaceMembership.ensureUserIsMember(members, query.authenticatedUserId)

        val userIds = members.map { it.userId }
        val userIdToUserInfoMap = userInfoProvider.getUsersByIds(userIds)
        val userIdToRoleNameMap = roleInfoProvider.getRoleNames(query.spaceId, userIds)

        return userIds.map { userId ->
            val persona = userIdToUserInfoMap[userId]
            val role = userIdToRoleNameMap[userId]
            UserInSpace(
                userId = userId,
                spaceId = query.spaceId,
                userName = persona?.userName ?: "",
                firstName = persona?.firstName ?: "",
                lastName = persona?.lastName ?: "",
                role = role?.let { RoleInfo(it.id, it.name, it.isVisible) }
            )
        }
    }
}