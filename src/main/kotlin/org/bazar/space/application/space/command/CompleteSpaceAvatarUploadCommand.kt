package org.bazar.space.application.space.command

import java.util.UUID

data class CompleteSpaceAvatarUploadCommand(
    val fileId: UUID,
    val status: AvatarUploadStatus,
    val validationErrors: List<AvatarUploadValidationError>?
)

enum class AvatarUploadStatus {
    UPLOADED, ERROR, VALIDATION_ERROR, UNKNOWN
}

data class AvatarUploadValidationError(
    val code: String,
    val description: String
)