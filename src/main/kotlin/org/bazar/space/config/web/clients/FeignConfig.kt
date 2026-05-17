package org.bazar.space.config.web.clients

import feign.RequestInterceptor
import org.bazar.space.util.getCurrentJwt
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders

@Configuration
class FeignConfig {

    @Bean
    fun feignAuthInterceptor(): RequestInterceptor {
        return RequestInterceptor { template ->
            template.header(HttpHeaders.AUTHORIZATION, "Bearer ${getCurrentJwt()}")
        }
    }

}