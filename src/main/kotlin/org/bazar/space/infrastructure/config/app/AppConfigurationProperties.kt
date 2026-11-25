package org.bazar.space.infrastructure.config.app

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app")
data class AppConfigurationProperties(
    val database: AppDatabaseProps,
    val joinSpace: AppJoinSpaceProps
)

data class AppDatabaseProps(
    val batchSize: Int
)

data class AppJoinSpaceProps(
    /** Invite link is built as `{baseJoinUrl}/{joinCode}` */
    val baseJoinUrl: String
)
