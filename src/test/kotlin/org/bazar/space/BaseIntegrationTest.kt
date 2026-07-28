package org.bazar.space

import org.bazar.authorization.sdk.BazarAuthorizationAdminClient
import org.bazar.authorization.sdk.BazarAuthorizationClient
import org.bazar.space.config.SharedAppContext.kafka
import org.bazar.space.config.SharedAppContext.postgres
import org.bazar.space.config.TestConfig
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.Sql.ExecutionPhase

@SpringBootTest
@ActiveProfiles("test")
@Import(TestConfig::class)
@Sql("classpath:db/scripts/clearTables.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
abstract class BaseIntegrationTest {

    @MockitoBean
    lateinit var bazarAuthorizationClient: BazarAuthorizationClient

    @MockitoBean
    lateinit var bazarAuthorizationAdminClient: BazarAuthorizationAdminClient

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