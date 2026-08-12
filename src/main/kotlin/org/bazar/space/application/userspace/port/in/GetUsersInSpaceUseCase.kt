package org.bazar.space.application.port.`in`

import org.bazar.space.application.userspace.query.GetUsersInSpaceQuery
import org.bazar.space.domain.userspace.UserInSpace

interface GetUsersInSpaceUseCase {
    fun execute(query: GetUsersInSpaceQuery): List<UserInSpace>
}