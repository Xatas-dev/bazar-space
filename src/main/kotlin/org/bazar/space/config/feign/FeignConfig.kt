package org.bazar.space.config.feign

import feign.RequestInterceptor
import org.bazar.space.infrastructure.security.SpringCurrentUserProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders

@Configuration
class FeignConfig {

    @Bean
    fun feignAuthInterceptor(currentUser: SpringCurrentUserProvider): RequestInterceptor {
        return RequestInterceptor { template ->
            template.header(HttpHeaders.AUTHORIZATION, "Bearer ${currentUser.jwtToken}")
        }
    }

}