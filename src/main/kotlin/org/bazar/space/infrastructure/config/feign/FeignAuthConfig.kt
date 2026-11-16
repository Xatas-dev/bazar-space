package org.bazar.space.infrastructure.config.feign

import feign.RequestInterceptor
import org.bazar.space.application.shared.port.out.CurrentUserProvider
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpHeaders

class FeignAuthConfig {

    @Bean
    fun feignAuthInterceptor(currentUser: CurrentUserProvider): RequestInterceptor {
        return RequestInterceptor { template ->
            template.header(HttpHeaders.AUTHORIZATION, "Bearer ${currentUser.accessToken}")
        }
    }
}