package org.bazar.space.application.space.command

data class InitiateSpaceAvatarUploadCommand(
    val spaceId: Long,
    val fileName: String,
    val contentType: String,
    val sizeBytes: Long
)