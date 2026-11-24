package org.bazar.space.adapter.inbound.rest

import org.bazar.space.api.SpaceControllerApi
import org.bazar.space.application.space.command.CreateSpaceCommand
import org.bazar.space.application.space.command.DeleteSpaceCommand
import org.bazar.space.application.space.command.InitiateSpaceAvatarUploadCommand
import org.bazar.space.application.space.command.UpdateSpaceCommand
import org.bazar.space.application.port.`in`.CreateSpaceUseCase
import org.bazar.space.application.port.`in`.DeleteSpaceUseCase
import org.bazar.space.application.port.`in`.GetSpacesUseCase
import org.bazar.space.application.port.`in`.UpdateSpaceUseCase
import org.bazar.space.application.space.port.`in`.GetSpaceAvatarUseCase
import org.bazar.space.application.space.port.`in`.GetSpaceJoinCodeUseCase
import org.bazar.space.application.space.port.`in`.GetSpacePreviewByJoinCodeUseCase
import org.bazar.space.application.space.port.`in`.InitiateSpaceAvatarUploadUseCase
import org.bazar.space.application.space.port.out.SpaceAvatarRepositoryPort
import org.bazar.space.application.space.query.GetSpacesQuery
import org.bazar.space.application.shared.port.out.CurrentUserProvider
import org.bazar.space.infrastructure.config.app.AppConfigurationProperties
import org.bazar.space.model.AvatarUploadRequestDto
import org.bazar.space.model.AvatarUploadTicketDtoResponse
import org.bazar.space.model.GetSpaceAvatarsResponse
import org.bazar.space.model.GetSpaceDto
import org.bazar.space.model.GetSpacesResponse
import org.bazar.space.model.SpaceJoinLinkResponse
import org.bazar.space.model.SpacePreviewResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class SpaceController(
    private val createSpaceUseCase: CreateSpaceUseCase,
    private val deleteSpaceUseCase: DeleteSpaceUseCase,
    private val updateSpaceUseCase: UpdateSpaceUseCase,
    private val getSpacesUseCase: GetSpacesUseCase,
    private val initiateSpaceAvatarUploadUseCase: InitiateSpaceAvatarUploadUseCase,
    private val getSpaceAvatarUseCase: GetSpaceAvatarUseCase,
    private val getSpaceJoinCodeUseCase: GetSpaceJoinCodeUseCase,
    private val getSpacePreviewByJoinCodeUseCase: GetSpacePreviewByJoinCodeUseCase,
    private val spaceAvatarRepositoryPort: SpaceAvatarRepositoryPort,
    private val currentUser: CurrentUserProvider,
    private val appProperties: AppConfigurationProperties,
) : SpaceControllerApi {

    override fun getAllSpaces(): ResponseEntity<GetSpacesResponse> {
        val spaces = getSpacesUseCase.execute(GetSpacesQuery(currentUser.id))
        val avatarUrlsBySpaceId = spaceAvatarRepositoryPort
            .findActiveBySpaceIds(spaces.mapNotNull { it.id })
            .associate { it.spaceId to it.url }
        return ResponseEntity.ok(spaces.toGetSpacesResponse(avatarUrlsBySpaceId))
    }

    override fun createSpace(name: String): ResponseEntity<GetSpaceDto> {
        val space = createSpaceUseCase.execute(CreateSpaceCommand(currentUser.id, name))
        return ResponseEntity.ok(space.toGetSpaceDto())
    }

    override fun deleteSpace(@PathVariable spaceId: Long): ResponseEntity<Unit> {
        deleteSpaceUseCase.execute(DeleteSpaceCommand(spaceId))
        return ResponseEntity.ok().build()
    }

    override fun patchSpace(
        @PathVariable spaceId: Long,
        name: String
    ): ResponseEntity<GetSpaceDto> {
        val space = updateSpaceUseCase.execute(UpdateSpaceCommand(spaceId, name))
        val avatarUrl = spaceAvatarRepositoryPort.findActiveBySpaceId(spaceId)?.url
        return ResponseEntity.ok(space.toGetSpaceDto(avatarUrl))
    }

    override fun initiateAvatarUpload(
        @PathVariable spaceId: Long,
        @RequestBody avatarUploadRequestDto: AvatarUploadRequestDto
    ): ResponseEntity<AvatarUploadTicketDtoResponse> {
        val result = initiateSpaceAvatarUploadUseCase.execute(
            InitiateSpaceAvatarUploadCommand(
                spaceId = spaceId,
                fileName = avatarUploadRequestDto.fileName,
                contentType = avatarUploadRequestDto.contentType,
                sizeBytes = avatarUploadRequestDto.sizeBytes
            )
        )
        return ResponseEntity.ok(AvatarUploadTicketDtoResponse(result.avatarId, result.uploadUrl))
    }

    override fun getSpaceAvatar(
        @PathVariable spaceId: Long
    ): ResponseEntity<GetSpaceAvatarsResponse> {
        val avatars = getSpaceAvatarUseCase.execute(spaceId)
        return ResponseEntity.ok(avatars.toGetSpaceAvatarsResponse())
    }

    override fun getSpaceJoinLink(
        @PathVariable spaceId: Long
    ): ResponseEntity<SpaceJoinLinkResponse> {
        val joinCode = getSpaceJoinCodeUseCase.execute(spaceId)
        return ResponseEntity.ok(joinCode.toSpaceJoinLinkResponse(appProperties.joinLink.baseJoinUrl))
    }

    override fun getSpacePreviewByJoinCode(
        @PathVariable code: String
    ): ResponseEntity<SpacePreviewResponse> {
        val preview = getSpacePreviewByJoinCodeUseCase.execute(code)
        return ResponseEntity.ok(preview.toSpacePreviewResponse())
    }
}
