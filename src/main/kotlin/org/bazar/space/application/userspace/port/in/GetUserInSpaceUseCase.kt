package org.bazar.space.application.userspace.port.`in`

import org.bazar.space.application.userspace.query.GetUserInSpaceQuery
import org.bazar.space.application.userspace.UserInSpace

interface GetUserInSpaceUseCase {
    fun execute(query: GetUserInSpaceQuery): UserInSpace
}
