package org.bazar.space.application.joinrequest.port.`in`.impl

import org.bazar.space.application.joinrequest.JoinRequestUser
import org.bazar.space.application.joinrequest.JoinRequestWithUser
import org.bazar.space.application.joinrequest.port.`in`.GetJoinRequestsUseCase
import org.bazar.space.application.joinrequest.port.out.SpaceJoinRequestRepositoryPort
import org.bazar.space.application.joinrequest.query.GetJoinRequestsQuery
import org.bazar.space.application.shared.PageResult
import org.bazar.space.application.shared.buildAuthorizationCheck
import org.bazar.space.application.shared.port.out.Authorizer
import org.bazar.space.application.space.port.out.SpaceRepositoryPort
import org.bazar.space.application.userspace.port.out.UserInfoProvider
import org.bazar.space.domain.auth.Permission
import org.bazar.space.domain.exception.SpaceNotFoundException
import org.springframework.stereotype.Service

@Service
class GetJoinRequestsUseCaseImpl(
    private val authorizer: Authorizer,
    private val spaceRepositoryPort: SpaceRepositoryPort,
    private val spaceJoinRequestRepositoryPort: SpaceJoinRequestRepositoryPort,
    private val userInfoProvider: UserInfoProvider
) : GetJoinRequestsUseCase {

    override fun execute(query: GetJoinRequestsQuery): PageResult<JoinRequestWithUser> {
        authorizer.authorizeOrThrow(buildAuthorizationCheck(query.spaceId, Permission.SPACE_USER_ADD))
        spaceRepositoryPort.findById(query.spaceId) ?: throw SpaceNotFoundException(query.spaceId)

        val requestsPage = spaceJoinRequestRepositoryPort.findPageBySpaceId(query.spaceId, query.page, query.size)
        val requests = requestsPage.items

        // one batch call for the whole page
        val usersById = if (requests.isEmpty()) emptyMap()
        else userInfoProvider.getUsersByIds(requests.map { it.userId }.distinct())

        val items = requests.map { request ->
            val user = usersById[request.userId]
            JoinRequestWithUser(
                id = requireNotNull(request.id) { "Persisted join request must have id" },
                user = JoinRequestUser(
                    userId = request.userId,
                    username = user?.username,
                    displayName = user?.displayName,
                    avatarUrl = user?.avatarUrl
                ),
                createdAt = request.createdAt
            )
        }
        return PageResult(items = items, page = requestsPage.page, size = requestsPage.size, total = requestsPage.total)
    }
}
