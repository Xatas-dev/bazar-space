package org.bazar.space.domain.auth

data class AuthorizationCheck(
    val spaceId: Long,
    val permission: Permission,
    val resourceId: String = "",
    val principalAttributes: Map<String, String> = emptyMap(),
    val resourceAttributes: Map<String, String> = emptyMap()
)
