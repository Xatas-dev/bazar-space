package org.bazar.space.infrastructure.client

import org.bazar.space.infrastructure.client.GetRoleNamesResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import java.util.*

@FeignClient(
    name = "bazarAuthorizationFeignClient",
    url = $$"${rest.clients.bazar-authz-url}"
)
interface BazarAuthorizationFeignClient {

    @GetMapping("/v1/space-users/roles")
    fun getRoleNames(
        @RequestParam("spaceId") spaceId: Long,
        @RequestParam("userIds") userIds: Collection<UUID>
    ): GetRoleNamesResponse
}

