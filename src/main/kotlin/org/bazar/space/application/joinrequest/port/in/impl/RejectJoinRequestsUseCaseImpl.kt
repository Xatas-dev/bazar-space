package org.bazar.space.application.joinrequest.port.`in`.impl

import org.bazar.space.application.joinrequest.command.RejectJoinRequestsCommand
import org.bazar.space.application.joinrequest.port.`in`.RejectJoinRequestsUseCase
import org.bazar.space.application.joinrequest.port.out.SpaceJoinRequestRepositoryPort
import org.bazar.space.application.shared.buildAuthorizationCheck
import org.bazar.space.application.shared.port.out.AppConfigProvider
import org.bazar.space.application.shared.port.out.Authorizer
import org.bazar.space.application.shared.port.out.TransactionRunner
import org.bazar.space.application.space.port.out.SpaceRepositoryPort
import org.bazar.space.domain.auth.Permission
import org.bazar.space.domain.exception.SpaceNotFoundException
import org.springframework.stereotype.Service

@Service
class RejectJoinRequestsUseCaseImpl(
    private val authorizer: Authorizer,
    private val transactionRunner: TransactionRunner,
    private val appConfigProvider: AppConfigProvider,
    private val spaceJoinRequestRepositoryPort: SpaceJoinRequestRepositoryPort,
    private val spaceRepositoryPort: SpaceRepositoryPort
) : RejectJoinRequestsUseCase {

    override fun execute(command: RejectJoinRequestsCommand): Int {
        authorizer.authorizeOrThrow(buildAuthorizationCheck(command.spaceId, Permission.SPACE_USER_ADD))
        spaceRepositoryPort.findById(command.spaceId) ?: throw SpaceNotFoundException(command.spaceId)
        return when (command) {
            is RejectJoinRequestsCommand.Selected -> rejectSelected(command.spaceId, command.requestIds)
            is RejectJoinRequestsCommand.All -> rejectAll(command.spaceId)
        }
    }

    private fun rejectAll(spaceId: Long): Int {
        var rejectedCount = 0
        while (true) {
            val batchRejectedCount = transactionRunner.inTransaction {
                spaceJoinRequestRepositoryPort.deleteBatchBySpaceId(spaceId, appConfigProvider.batchSize)
            }
            if (batchRejectedCount == 0) return rejectedCount
            rejectedCount += batchRejectedCount
        }
    }

    private fun rejectSelected(spaceId: Long, requestIds: Set<Long>): Int =
        requestIds.chunked(appConfigProvider.batchSize).sumOf { batchIds ->
            transactionRunner.inTransaction { spaceJoinRequestRepositoryPort.deleteBySpaceIdAndIds(spaceId, batchIds) }
        }
}
