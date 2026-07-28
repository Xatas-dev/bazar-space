package org.bazar.space.application.usecase

import org.bazar.space.domain.model.RoleInfo
import org.bazar.space.domain.model.UserInSpace
import org.bazar.space.domain.port.RoleInfoProvider
import org.bazar.space.domain.port.UserInfoProvider
import org.bazar.space.domain.port.UserSpaceRepositoryPort
import org.bazar.space.util.exceptions.ApiException
import org.bazar.space.util.exceptions.ApiExceptions
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class GetUsersInSpaceUseCase(
    private val userSpaceRepositoryPort: UserSpaceRepositoryPort,
    private val userInfoProvider: UserInfoProvider,
    private val roleInfoProvider: RoleInfoProvider
) {

    @Transactional(readOnly = true)
    fun execute(spaceId: Long, authenticatedUserId: UUID): List<UserInSpace> {
        val userIds = userSpaceRepositoryPort.findAllBySpaceId(spaceId).map { it.userId }.toList()

        if (!userIds.contains(authenticatedUserId)) {
            throw ApiException(ApiExceptions.FORBIDDEN)
        }

        val userIdToUserInfoMap = userInfoProvider.getUsersByIds(userIds)
        val userIdToRoleNameMap = roleInfoProvider.getRoleNames(spaceId, userIds)

        return userIds.map { userId ->
            val persona = userIdToUserInfoMap[userId]
            val role = userIdToRoleNameMap[userId]
            UserInSpace(
                userId = userId,
                spaceId = spaceId,
                userName = persona?.userName ?: "",
                firstName = persona?.firstName ?: "",
                lastName = persona?.lastName ?: "",
                role = role?.let { RoleInfo(it.id, it.name, it.isVisible) }
            )
        }
    }
}
