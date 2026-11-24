package org.bazar.space.application.space.port.`in`

fun interface GetSpacePreviewByJoinCodeUseCase {
    fun execute(joinCode: String): SpacePreview
}
