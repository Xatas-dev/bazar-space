package org.bazar.space.application.space.port.`in`.impl

import org.bazar.space.application.space.command.InitiateSpaceAvatarUploadCommand
import org.bazar.space.application.space.port.`in`.InitiateAvatarUploadResult
import org.bazar.space.application.space.port.`in`.InitiateSpaceAvatarUploadUseCase
import org.bazar.space.application.space.port.out.SpaceAvatarRepositoryPort
import org.bazar.space.application.space.port.out.SpaceRepositoryPort
import org.bazar.space.application.space.port.out.files.FileStoragePort
import org.bazar.space.application.space.port.out.files.GetUploadUrlRequest
import org.bazar.space.application.shared.buildAuthorizationCheck
import org.bazar.space.application.shared.port.out.Authorizer
import org.bazar.space.domain.auth.Permission
import org.bazar.space.domain.exception.SpaceNotFoundException
import org.bazar.space.domain.space.SpaceAvatar
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class InitiateSpaceAvatarUploadUseCaseImpl(
    private val spaceRepositoryPort: SpaceRepositoryPort,
    private val fileStoragePort: FileStoragePort,
    private val spaceAvatarRepositoryPort: SpaceAvatarRepositoryPort,
    private val authorizer: Authorizer
) : InitiateSpaceAvatarUploadUseCase {

    @Transactional
    override fun execute(command: InitiateSpaceAvatarUploadCommand): InitiateAvatarUploadResult {
        authorizer.authorizeOrThrow(buildAuthorizationCheck(command.spaceId, Permission.SPACE_WRITE))
        spaceRepositoryPort.findById(command.spaceId)
            ?: throw SpaceNotFoundException(command.spaceId)

        val uploadData = fileStoragePort.initiateUpload(
            GetUploadUrlRequest(
                fileName = command.fileName,
                size = command.sizeBytes,
                contentType = command.contentType
            )
        )
        val avatar = spaceAvatarRepositoryPort.insert(
            SpaceAvatar.createAvatarForSpace(command.spaceId, uploadData.fileUuid)
        )
        return InitiateAvatarUploadResult(
            avatarId = avatar.id!!,
            uploadUrl = uploadData.uploadUrl
        )
    }
}