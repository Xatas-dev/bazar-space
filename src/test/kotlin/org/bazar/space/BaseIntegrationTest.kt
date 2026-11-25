package org.bazar.space

import org.bazar.space.infrastructure.config.SharedAppContext.kafka
import org.bazar.space.infrastructure.config.SharedAppContext.postgres
import org.bazar.space.infrastructure.config.TestConfig
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.Sql.ExecutionPhase

@SpringBootTest(classes = [BazarSpaceApplication::class])
@ActiveProfiles("test")
@Import(TestConfig::class)
@Sql("classpath:db/scripts/clearTables.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
abstract class BaseIntegrationTest {

    companion object {
        @JvmStatic
        @DynamicPropertySource
        fun datasourceConfig(registry: DynamicPropertyRegistry) {

            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
            registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers)
        }
    }

}
