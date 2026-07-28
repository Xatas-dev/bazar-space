package org.bazar.space.config.app

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(AppConfigurationProperties::class)
class AppConfiguration {
}