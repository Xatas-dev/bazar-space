package org.bazar.space.infrastructure.config.cache

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*
import java.util.concurrent.TimeUnit

@Configuration
@EnableCaching
class CacheConfig {

    @Bean
    fun cacheManager(): CacheManager {
        val cacheManager = CaffeineCacheManager(Caches.USER_INFO_CACHE.name, Caches.AUTHORIZATION_USER_ROLES_CACHE.name)
        cacheManager.setCaffeine(
            Caffeine.newBuilder()
                .expireAfterWrite(3, TimeUnit.MINUTES)
                .maximumSize(10000)
        )
        return cacheManager
    }
}

enum class Caches {
    USER_INFO_CACHE, AUTHORIZATION_USER_ROLES_CACHE
}

data class RoleNameCacheKey(
    val spaceId: Long,
    val userId: UUID
) {
    override fun toString(): String {
        return "$spaceId:$userId"
    }
}

