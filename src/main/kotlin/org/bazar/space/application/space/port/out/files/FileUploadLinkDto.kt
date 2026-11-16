package org.bazar.space.application.space.port.out.files

import java.util.UUID

data class FileUploadLinkDto(
    val uploadUrl: String,
    val fileUuid: UUID
)