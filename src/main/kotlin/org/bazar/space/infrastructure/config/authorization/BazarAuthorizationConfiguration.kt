package org.bazar.space.infrastructure.config.authorization

import org.bazar.authorization.sdk.BazarAuthorizationAdminClient
import org.bazar.authorization.sdk.BazarAuthorizationClient
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(BazarAuthorizationConfigProperties::class)
class BazarAuthorizationConfiguration(
    private val properties: BazarAuthorizationConfigProperties
) {

    @Bean
    fun bazarAuthorizationClient(): BazarAuthorizationClient {
        return BazarAuthorizationClient.builder()
            .port(properties.port)
            .host(properties.host)
            .build()
    }

    @Bean
    fun bazarAuthorizationAdminClient(): BazarAuthorizationAdminClient {
        return BazarAuthorizationAdminClient.builder()
            .host(properties.host)
            .port(properties.port)
            .build()
    }

}