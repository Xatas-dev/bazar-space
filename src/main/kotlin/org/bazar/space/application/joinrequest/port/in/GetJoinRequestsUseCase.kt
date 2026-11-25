package org.bazar.space.application.joinrequest.port.`in`

import org.bazar.space.application.joinrequest.JoinRequestWithUser
import org.bazar.space.application.joinrequest.query.GetJoinRequestsQuery
import org.bazar.space.application.shared.PageResult

fun interface GetJoinRequestsUseCase {
    fun execute(query: GetJoinRequestsQuery): PageResult<JoinRequestWithUser>
}
