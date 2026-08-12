package org.bazar.space.adapter.outbound.client

import feign.FeignException
import io.github.oshai.kotlinlogging.KotlinLogging
import org.bazar.space.infrastructure.config.cache.Caches
import org.bazar.space.infrastructure.config.cache.RoleNameCacheKey
import org.bazar.space.application.userspace.port.out.GetRoleNameDto
import org.bazar.space.application.userspace.port.out.RoleInfoProvider
import org.bazar.space.infrastructure.util.extension.toUuid
import org.springframework.cache.CacheManager
import org.springframework.cache.get
import org.springframework.stereotype.Component
import java.util.*

@Component
class BazarAuthorizationHttpClient(
    private val bazarAuthorizationFeignClient: BazarAuthorizationFeignClient,
    private val cacheManager: CacheManager
) : RoleInfoProvider {

    private val logger = KotlinLogging.logger { }

    override fun getRoleNames(spaceId: Long, userIds: List<UUID>): Map<UUID, GetRoleNameDto> {
        logger.debug { "Fetching role names from bazar-authorization: ${userIds.size} users" }

        val cacheKeys = userIds.map { RoleNameCacheKey(spaceId, it) }
        val cacheHit: Map<UUID, GetRoleNameDto> = getRoleNamesFromCache(cacheKeys)
        val cacheMiss: List<UUID> = userIds.filter { !cacheHit.contains(it) }

        if (cacheMiss.isEmpty()) {
            return cacheHit
        }

        val response = try {
            bazarAuthorizationFeignClient.getRoleNames(spaceId, cacheMiss)
        } catch (ex: FeignException) {
            logger.error(ex) { "Error during bazar-authorization api call, can't enrich original response with role name" }
            GetRoleNamesResponse(emptyList())
        }

        val usersResponse = response.roles.associateBy { it.userId.toUuid() }
        putInCache(usersResponse.mapKeys { RoleNameCacheKey(spaceId, it.key).toString() })

        return cacheHit + usersResponse
    }

    private fun getRoleNamesFromCache(keys: List<RoleNameCacheKey>): Map<UUID, GetRoleNameDto> {
        val cache = cacheManager.getCache(Caches.AUTHORIZATION_USER_ROLES_CACHE.name) ?: run {
            logger.warn { "Cache for authorizationUserRoles is not available, fallback to regular Feign calls" }
            return emptyMap()
        }
        return keys.mapNotNull { key ->
            cache.get<GetRoleNameDto>(key.toString())?.let {
                key.userId to it
            }
        }.toMap()
    }

    private fun putInCache(userIdToUserDtoMap: Map<String, GetRoleNameDto>) {
        val cache = cacheManager.getCache(Caches.AUTHORIZATION_USER_ROLES_CACHE.name) ?: run {
            logger.warn { "Cache for authorizationUserRoles is not available, can't put" }
            return
        }

        userIdToUserDtoMap.forEach {
            cache.put(it.key, it.value)
        }
    }
}

data class GetRoleNamesResponse(
    val roles: List<GetRoleNameDto>
)

