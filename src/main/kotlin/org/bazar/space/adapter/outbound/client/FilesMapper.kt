package org.bazar.space.adapter.outbound.client

import org.bazar.space.application.space.port.out.files.FileUploadLinkDto
import org.bazar.space.application.space.port.out.files.GetUploadUrlRequest
import java.util.UUID

fun GetUploadUrlRequest.toFeignRequest() = InitiateUploadRequestDtoV1(
    fileName = fileName,
    size = size,
    contentType = contentType,
    domain = "bazar-space",
    accessType = "PUBLIC"
)

fun InitiateUploadResponseDtoV1.toFileUploadLinkDto(): FileUploadLinkDto =
    FileUploadLinkDto(uploadUrl = uploadUrl, fileUuid = UUID.fromString(fileUuid))