package org.bazar.space.infrastructure.config

import org.bazar.space.utils.repository.JdbcTestHelper
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.jdbc.core.JdbcTemplate

@TestConfiguration
class TestConfig {
    @Bean
    fun jdbcTestHelper(jdbcTemplate: JdbcTemplate): JdbcTestHelper = JdbcTestHelper(jdbcTemplate)
}