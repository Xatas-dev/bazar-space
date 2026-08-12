package org.bazar.space.application.userspace.port.out

import org.bazar.space.domain.userspace.UserSpace
import java.util.UUID

interface UserSpaceRepositoryPort {
    fun save(userSpace: UserSpace): UserSpace

    fun findAllBySpaceId(spaceId: Long): List<UserSpace>

    fun deleteAllBySpaceId(spaceId: Long)

    fun deleteBySpaceIdAndUserId(spaceId: Long, userId: UUID)

    fun findBySpaceIdAndUserId(spaceId: Long, userId: UUID): UserSpace?
}
