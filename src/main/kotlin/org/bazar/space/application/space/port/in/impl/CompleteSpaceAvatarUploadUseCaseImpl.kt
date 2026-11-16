package org.bazar.space.application.space.port.`in`.impl

import org.bazar.space.application.space.command.AvatarUploadStatus
import org.bazar.space.application.space.command.AvatarUploadValidationError
import org.bazar.space.application.space.command.CompleteSpaceAvatarUploadCommand
import org.bazar.space.application.space.port.`in`.CompleteSpaceAvatarUploadUseCase
import org.bazar.space.application.space.port.out.SpaceAvatarRepositoryPort
import org.bazar.space.application.space.port.out.files.CompleteUploadRequest
import org.bazar.space.application.space.port.out.files.FileStoragePort
import org.bazar.space.domain.exception.SpaceAvatarNotFoundException
import org.bazar.space.domain.space.SpaceAvatar
import org.bazar.space.domain.space.SpaceAvatarStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CompleteSpaceAvatarUploadUseCaseImpl(
    private val fileStoragePort: FileStoragePort,
    private val spaceAvatarRepositoryPort: SpaceAvatarRepositoryPort
) : CompleteSpaceAvatarUploadUseCase {

    @Transactional
    override fun execute(command: CompleteSpaceAvatarUploadCommand) {
        val existingAvatar = spaceAvatarRepositoryPort.findByFileId(command.fileId)
            ?: throw SpaceAvatarNotFoundException(command.fileId)

        if (existingAvatar.status != SpaceAvatarStatus.PENDING) {
            return
        }

        val updatedAvatar = when (command.status) {
            AvatarUploadStatus.UPLOADED -> onSuccess(existingAvatar)
            else -> onFail(existingAvatar, command.validationErrors)
        }

        spaceAvatarRepositoryPort.update(updatedAvatar)
    }

    private fun onSuccess(avatar: SpaceAvatar): SpaceAvatar {
        val prevActiveAvatar = spaceAvatarRepositoryPort.findActiveBySpaceId(avatar.spaceId)
        val downloadLink = fileStoragePort.getDownloadLink(CompleteUploadRequest(avatar.fileId))

        if (prevActiveAvatar != null) {
            spaceAvatarRepositoryPort.update(prevActiveAvatar.setActive(false))
        }

        return avatar.uploadSuccess(downloadLink.url)
    }

    private fun onFail(avatar: SpaceAvatar, errors: List<AvatarUploadValidationError>?): SpaceAvatar {
        return avatar.uploadFailed(
            errors?.map { "${it.code}: ${it.description}" }?.toString() ?: "Failed, but was no errors"
        )
    }
}