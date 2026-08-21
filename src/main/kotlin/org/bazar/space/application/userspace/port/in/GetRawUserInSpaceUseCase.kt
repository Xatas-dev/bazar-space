package org.bazar.space.application.userspace.port.`in`

import org.bazar.space.application.userspace.query.GetUserInSpaceQuery
import org.bazar.space.application.userspace.UserInSpaceRaw

interface GetRawUserInSpaceUseCase {
    fun execute(query: GetUserInSpaceQuery): UserInSpaceRaw
}
