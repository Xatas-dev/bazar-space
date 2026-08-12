package org.bazar.space.infrastructure.config.authorization

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "authorization")
data class BazarAuthorizationConfigProperties(
    val port: Int,
    val host: String
)