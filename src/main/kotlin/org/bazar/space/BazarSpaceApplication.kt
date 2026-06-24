package org.bazar.space

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.resilience.annotation.EnableResilientMethods

@SpringBootApplication
@EnableFeignClients
@EnableCaching
@EnableResilientMethods
class BazarSpaceApplication

fun main(args: Array<String>) {
    runApplication<BazarSpaceApplication>(*args)
}