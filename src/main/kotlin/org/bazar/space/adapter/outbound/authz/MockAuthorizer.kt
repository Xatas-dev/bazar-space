package org.bazar.space.adapter.outbound.authz

import io.github.oshai.kotlinlogging.KotlinLogging
import org.bazar.space.domain.auth.AuthorizationCheck
import org.bazar.space.application.shared.port.out.Authorizer
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.stereotype.Service

@Service
@ConditionalOnMissingBean(AuthorizerAdapter::class)
class MockAuthorizer : Authorizer {

    private val logger = KotlinLogging.logger { }

    init {
        logger.info { "Using mock authorizer" }
    }

    override fun authorizeOrThrow(check: AuthorizationCheck) {
        logger.info { "Authorization check: $check" }
    }
}
