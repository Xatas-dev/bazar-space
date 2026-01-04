package org.bazar.space.config.grpc

import io.grpc.ClientInterceptor
import org.bazar.authorization.grpc.AuthorizationAdminServiceGrpc
import org.bazar.authorization.grpc.AuthorizationAdminServiceGrpc.AuthorizationAdminServiceBlockingStub
import org.bazar.authorization.grpc.AuthorizationServiceGrpc
import org.bazar.authorization.grpc.AuthorizationServiceGrpc.AuthorizationServiceBlockingStub
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.grpc.client.GlobalClientInterceptor
import org.springframework.grpc.client.ImportGrpcClients
import org.springframework.grpc.client.interceptor.security.BearerTokenAuthenticationInterceptor
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt


@Configuration
@ImportGrpcClients(target = "admin", types = [AuthorizationAdminServiceBlockingStub::class])
@ImportGrpcClients(target = "authz", types = [AuthorizationServiceBlockingStub::class])
class GrpcConfiguration {

    @Bean
    @GlobalClientInterceptor
    fun bearerInterceptor(): ClientInterceptor {
        return BearerTokenAuthenticationInterceptor { (SecurityContextHolder.getContext().authentication?.principal as Jwt).tokenValue }
    }
}