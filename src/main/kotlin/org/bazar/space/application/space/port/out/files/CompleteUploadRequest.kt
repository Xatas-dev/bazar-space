package org.bazar.space.application.space.port.out.files

import java.util.UUID

data class CompleteUploadRequest(
    val fileId: UUID
)