package org.bazar.space.adapter.outbound.client

import org.bazar.space.application.userspace.port.out.UserPersonaDto
import org.bazar.space.infrastructure.config.feign.FeignAuthConfig
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import java.util.*

@FeignClient(
    name = "bazarPersonaFeignClient",
    url = $$"${rest.clients.bazar-persona-url}",
    configuration = [FeignAuthConfig::class]
)
interface BazarPersonaFeignClient {

    @GetMapping("/v2/users")
    fun getUsersByIds(@RequestParam("ids") ids: Collection<UUID>): List<UserPersonaDto>
}

