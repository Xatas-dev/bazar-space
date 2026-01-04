package org.bazar.space.repository

import org.bazar.space.entity.Space
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface SpaceRepository: JpaRepository<Space, Long> {

    @Query("""
        select s from Space s
        join UserSpace us on us.space.id = s.id
        where us.userId = :userId
    """)
    fun findAllByUserId(userId: UUID): List<Space>

}