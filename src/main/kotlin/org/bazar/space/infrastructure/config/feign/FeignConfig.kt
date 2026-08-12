package org.bazar.space.infrastructure.config.feign

import feign.RequestInterceptor
import org.bazar.space.application.shared.port.out.CurrentUserProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders

@Configuration
class FeignConfig {

    @Bean
    fun feignAuthInterceptor(currentUser: CurrentUserProvider): RequestInterceptor {
        return RequestInterceptor { template ->
            template.header(HttpHeaders.AUTHORIZATION, "Bearer ${currentUser.accessToken}")
        }
    }

}