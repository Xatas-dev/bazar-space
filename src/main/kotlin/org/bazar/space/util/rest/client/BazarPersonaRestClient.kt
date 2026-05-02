package org.bazar.space.util.rest.client

import io.github.oshai.kotlinlogging.KotlinLogging
import org.bazar.space.config.cache.Caches
import org.bazar.space.util.rest.BazarPersonaFeignClient
import org.springframework.cache.CacheManager
import org.springframework.cache.get
import org.springframework.stereotype.Component
import java.util.*

@Component
class BazarPersonaClient(
    private val bazarPersonaFeignClient: BazarPersonaFeignClient,
    private val cacheManager: CacheManager
) {

    private val logger = KotlinLogging.logger { }

    fun getUsersByIds(userIds: List<UUID>): Map<UUID, UserPersonaDto> {
        logger.debug { "Fetching multiple users from bazar-persona: ${userIds.size} users" }

        val usersInCache = getUsersFromCache(userIds)
        val cacheMissedUserIds = userIds.filter { !usersInCache.contains(it) }

        if (cacheMissedUserIds.isEmpty()) {
            return usersInCache
        }

        val response = bazarPersonaFeignClient.getUsersByIds(cacheMissedUserIds)
        val usersResponse = response.associateBy { it.id }
        putInCache(usersResponse)

        return usersInCache + usersResponse
    }

    private fun getUsersFromCache(userIds: List<UUID>): Map<UUID, UserPersonaDto> {
        val cache = cacheManager.getCache(Caches.USER_INFO_CACHE.name) ?: run {
            logger.warn { "Cache for userInfo is not available, fallback to regular REST calls" }
            return emptyMap()
        }
        return userIds.mapNotNull { userId ->
            cache.get<UserPersonaDto>(userId)?.let {
                userId to it
            }
        }.toMap()
    }

    private fun putInCache(userIdToUserDtoMap: Map<UUID, UserPersonaDto>) {
        val cache = cacheManager.getCache(Caches.USER_INFO_CACHE.name) ?: run {
            logger.warn { "Cache for userInfo is not available, can't put" }
            return
        }

        userIdToUserDtoMap.forEach {
            cache.put(it.key, it.value)
        }
    }

}

data class UserPersonaDto(
    val id: UUID,
    val userName: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val userPic: String? = null
)


