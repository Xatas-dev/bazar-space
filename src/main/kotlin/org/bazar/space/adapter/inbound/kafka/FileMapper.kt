package org.bazar.space.adapter.inbound.kafka

import org.bazar.space.application.space.command.AvatarUploadStatus
import org.bazar.space.application.space.command.AvatarUploadValidationError
import org.bazar.space.application.space.command.CompleteSpaceAvatarUploadCommand

fun BazarFilesEvent.toCompleteSpaceAvatarUploadCommand(): CompleteSpaceAvatarUploadCommand =
    CompleteSpaceAvatarUploadCommand(
        fileId = fileUuid,
        status = status.toAvatarUploadStatus(),
        validationErrors = errors?.map { it.toAvatarUploadValidationError() }
    )

fun FileUploadStatus.toAvatarUploadStatus(): AvatarUploadStatus =
    AvatarUploadStatus.valueOf(name)

fun FileUploadError.toAvatarUploadValidationError(): AvatarUploadValidationError =
    AvatarUploadValidationError(code = code, description = description)