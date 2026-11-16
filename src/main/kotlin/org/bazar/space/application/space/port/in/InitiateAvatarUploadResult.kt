package org.bazar.space.application.space.port.`in`

data class InitiateAvatarUploadResult(
    val avatarId: Long,
    val uploadUrl: String
)