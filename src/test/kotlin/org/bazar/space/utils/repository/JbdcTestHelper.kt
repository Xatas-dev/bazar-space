package org.bazar.space.utils.repository

import org.springframework.jdbc.core.DataClassRowMapper
import org.springframework.jdbc.core.JdbcTemplate

class JdbcTestHelper(val jdbcTemplate: JdbcTemplate) {

    inline fun <reified T : Any> findAll(table: String): List<T> {
        return jdbcTemplate.query(
            "SELECT * FROM $table",
            DataClassRowMapper(T::class.java)
        )
    }
}