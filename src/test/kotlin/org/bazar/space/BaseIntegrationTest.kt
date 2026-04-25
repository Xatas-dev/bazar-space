package org.bazar.space

import org.bazar.authorization.sdk.BazarAuthorizationAdminClient
import org.bazar.authorization.sdk.BazarAuthorizationClient
import org.junit.jupiter.api.BeforeAll
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.Sql.ExecutionPhase
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.lifecycle.Startables
import org.testcontainers.utility.DockerImageName
import java.util.stream.Stream

@SpringBootTest
@ActiveProfiles("test")
@Sql("classpath:db/scripts/clearTables.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
abstract class BaseIntegrationTest {

    @MockitoBean
    lateinit var bazarAuthorizationClient: BazarAuthorizationClient
    @MockitoBean
    lateinit var bazarAuthorizationAdminClient: BazarAuthorizationAdminClient


    companion object {

        private val postgres: PostgreSQLContainer<*> = PostgreSQLContainer(DockerImageName.parse("postgres:16.0"))
            .apply {
                this.withDatabaseName("testDb").withUsername("test").withPassword("test")
            }

        @JvmStatic
        @DynamicPropertySource
        fun datasourceConfig(registry: DynamicPropertyRegistry) {
            Startables.deepStart(Stream.of(postgres)).join()

            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
        }

    }
}