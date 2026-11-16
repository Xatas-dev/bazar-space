package org.bazar.space.adapter.outbound.client

import org.bazar.space.application.space.port.out.files.CompleteUploadRequest
import org.bazar.space.application.space.port.out.files.FileDownloadLinkDto
import org.bazar.space.application.space.port.out.files.FileStoragePort
import org.bazar.space.application.space.port.out.files.FileUploadLinkDto
import org.bazar.space.application.space.port.out.files.GetUploadUrlRequest
import org.springframework.stereotype.Component

@Component
class BazarFilesStoragePort(
    private val bazarFilesFeignClient: BazarFilesFeignClient
) : FileStoragePort {

    override fun initiateUpload(request: GetUploadUrlRequest): FileUploadLinkDto =
        bazarFilesFeignClient.initiateUpload(request.toFeignRequest()).toFileUploadLinkDto()

    override fun getDownloadLink(request: CompleteUploadRequest): FileDownloadLinkDto {
        val response = bazarFilesFeignClient.initiateDownload(request.fileId.toString())
        return FileDownloadLinkDto(response.publicUrl)
    }
}