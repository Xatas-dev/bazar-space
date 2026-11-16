package org.bazar.space.adapter.outbound.client

data class InitiateUploadResponseDtoV1(
    val uploadUrl: String,
    val fileUuid: String
)