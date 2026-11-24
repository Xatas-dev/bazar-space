package org.bazar.space.application.space.port.`in`

fun interface GetSpaceJoinCodeUseCase {
    fun execute(spaceId: Long): String
}
