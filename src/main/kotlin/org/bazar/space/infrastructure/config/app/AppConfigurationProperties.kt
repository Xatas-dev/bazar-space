package org.bazar.space.infrastructure.config.app

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app")
data class AppConfigurationProperties(
    val database: AppDatabaseProps,
    val joinLink: AppJoinLinkProps
)

data class AppDatabaseProps(
    val batchSize: Int
)

data class AppJoinLinkProps(
    val baseJoinUrl: String
)
