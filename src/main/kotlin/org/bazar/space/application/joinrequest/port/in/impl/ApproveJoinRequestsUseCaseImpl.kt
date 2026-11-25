package org.bazar.space.application.joinrequest.port.`in`.impl

import org.bazar.space.application.joinrequest.command.ApproveJoinRequestsCommand
import org.bazar.space.application.joinrequest.port.`in`.ApproveJoinRequestsUseCase
import org.bazar.space.application.joinrequest.port.out.SpaceJoinRequestRepositoryPort
import org.bazar.space.application.shared.buildAuthorizationCheck
import org.bazar.space.application.shared.port.out.AppConfigProvider
import org.bazar.space.application.shared.port.out.Authorizer
import org.bazar.space.application.shared.port.out.TransactionRunner
import org.bazar.space.application.userspace.port.out.UserSpaceRepositoryPort
import org.bazar.space.domain.auth.Permission
import org.bazar.space.domain.joinrequest.SpaceJoinRequest
import org.bazar.space.domain.userspace.UserSpace
import org.springframework.stereotype.Service

@Service
class ApproveJoinRequestsUseCaseImpl(
    private val authorizer: Authorizer,
    private val transactionRunner: TransactionRunner,
    private val appConfigProvider: AppConfigProvider,
    private val userSpaceRepositoryPort: UserSpaceRepositoryPort,
    private val spaceJoinRequestRepositoryPort: SpaceJoinRequestRepositoryPort
) : ApproveJoinRequestsUseCase {

    override fun execute(command: ApproveJoinRequestsCommand): Int {
        authorizer.authorizeOrThrow(buildAuthorizationCheck(command.spaceId, Permission.SPACE_USER_ADD))
        return when (command) {
            is ApproveJoinRequestsCommand.Selected -> approveSelected(command.spaceId, command.requestIds)
            is ApproveJoinRequestsCommand.All -> approveAll(command.spaceId)
        }
    }

    private fun approveSelected(spaceId: Long, requestIds: Set<Long>): Int =
        requestIds.chunked(appConfigProvider.batchSize).sumOf { batchIds ->
            transactionRunner.inTransaction {
                approveBatch(spaceId, spaceJoinRequestRepositoryPort.findAllBySpaceIdAndIds(spaceId, batchIds))
            }
        }

    private fun approveAll(spaceId: Long): Int {
        var approvedCount = 0
        while (true) {
            val batchApprovedCount = transactionRunner.inTransaction {
                val batch = spaceJoinRequestRepositoryPort.findBySpaceId(spaceId, appConfigProvider.batchSize)
                if (batch.isEmpty()) null else approveBatch(spaceId, batch)
            } ?: return approvedCount
            approvedCount += batchApprovedCount
        }
    }

    private fun approveBatch(spaceId: Long, requests: List<SpaceJoinRequest>): Int {
        if (requests.isEmpty()) return 0
        userSpaceRepositoryPort.saveAll(requests.map { UserSpace.createMember(spaceId, it.userId) })
        return spaceJoinRequestRepositoryPort.deleteBySpaceIdAndIds(spaceId, requests.map { requireNotNull(it.id) })
    }
}
