package org.bazar.space.infrastructure.config.app

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app")
data class AppConfigurationProperties(
    val database: AppDatabaseProps
)

data class AppDatabaseProps(
    val batchSize: Int
)