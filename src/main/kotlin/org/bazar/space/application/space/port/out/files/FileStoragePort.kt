package org.bazar.space.application.space.port.out.files

interface FileStoragePort {
    fun initiateUpload(request: GetUploadUrlRequest): FileUploadLinkDto
    fun getDownloadLink(request: CompleteUploadRequest): FileDownloadLinkDto
}