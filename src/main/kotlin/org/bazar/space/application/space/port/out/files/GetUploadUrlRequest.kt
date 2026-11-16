package org.bazar.space.application.space.port.out.files

data class GetUploadUrlRequest(
    val fileName: String,
    val size: Long,
    val contentType: String
)