package org.bazar.space.repository

import org.bazar.space.entity.UserSpace
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserSpaceRepository: JpaRepository<UserSpace, Long> {


    fun deleteAllBySpaceId(spaceId: Long) : Long

    fun findAllBySpaceId(spaceId: Long): List<UserSpace>

    fun deleteBySpaceIdAndUserId(spaceId: Long, userId: UUID): Int
}