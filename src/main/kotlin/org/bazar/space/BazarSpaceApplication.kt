package org.bazar.space

import org.bazar.space.infrastructure.config.properties.ValidationProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication(scanBasePackages = ["org.bazar.space"])
@EnableFeignClients(basePackages = ["org.bazar.space"])
@EnableCaching
@EnableScheduling
@EnableConfigurationProperties(ValidationProperties::class)
class BazarSpaceApplication

fun main(args: Array<String>) {
    runApplication<BazarSpaceApplication>(*args)
}
