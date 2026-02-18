package org.bazar.space.persistence.repository

import org.bazar.space.persistence.entity.UserSpace
import org.bazar.space.persistence.rowmapper.UserSpaceRowMapper
import org.springframework.data.repository.CrudRepository
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserSpaceRepository : CrudRepository<UserSpace, Long> {

    fun findAllBySpaceId(spaceId: Long): List<UserSpace>

    fun deleteAllBySpaceId(spaceId: Long): Int

    fun deleteBySpaceIdAndUserId(spaceId: Long, userId: UUID): Int
}