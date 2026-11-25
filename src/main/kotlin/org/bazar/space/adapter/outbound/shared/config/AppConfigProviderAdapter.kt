package org.bazar.space.adapter.outbound.shared.config

import org.bazar.space.application.shared.port.out.AppConfigProvider
import org.bazar.space.infrastructure.config.app.AppConfigurationProperties
import org.springframework.stereotype.Component

@Component
class AppConfigProviderAdapter(
    private val appConfigurationProperties: AppConfigurationProperties
) : AppConfigProvider {

    override val batchSize: Int
        get() = appConfigurationProperties.database.batchSize

    override val baseJoinUrl: String
        get() = appConfigurationProperties.joinSpace.baseJoinUrl
}
