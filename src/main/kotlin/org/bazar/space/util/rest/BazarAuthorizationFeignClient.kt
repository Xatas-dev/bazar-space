package org.bazar.space.util.rest

import org.bazar.space.util.rest.client.GetRoleNamesResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import java.util.*

@FeignClient(
    name = "bazarAuthorizationFeignClient",
    url = $$"${rest.clients.bazar-authz-url}"
)
interface BazarAuthorizationFeignClient {

    @GetMapping("/space-users/role-names")
    fun getRoleNames(
        @RequestParam("spaceId") spaceId: Long,
        @RequestParam("userIds") userIds: Collection<UUID>
    ): GetRoleNamesResponse
}

