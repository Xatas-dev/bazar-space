package org.bazar.space.config.authorization.client

import io.github.oshai.kotlinlogging.KotlinLogging
import io.grpc.StatusRuntimeException
import org.bazar.authorization.grpc.*
import org.bazar.authorization.grpc.AuthorizationAdminServiceGrpc.AuthorizationAdminServiceBlockingStub
import org.bazar.authorization.grpc.AuthorizationServiceGrpc.AuthorizationServiceBlockingStub
import org.bazar.space.model.http.UserSpaceRole
import org.bazar.space.service.authorization.AuthorizationAction
import org.bazar.space.util.extension.toApiException
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.stereotype.Component
import java.util.*

@Component
@ConditionalOnMissingBean(MockAuthorizationGrpcClient::class)
class AuthorizationGrpcClientImpl(
    private val grpcAdminClient: AuthorizationAdminServiceBlockingStub,
    private val grpcBasicClient: AuthorizationServiceBlockingStub
) : AuthorizationGrpcClient {

    private val logger = KotlinLogging.logger { }

    override fun tryToCreateSpaceInAuthz(spaceId: Long) {
        try {
            grpcAdminClient.createSpace(
                CreateSpaceRequest.newBuilder()
                    .setSpaceId(spaceId)
                    .build()
            )
        } catch (ex: StatusRuntimeException) {
            logger.error(ex) { "Error during adding new space owner to authorization, spaceId = $spaceId" }
            throw ex.toApiException()
        }
    }

    override fun tryToAddUserToSpaceInAuthz(spaceId: Long, userId: UUID) {
        try {
            grpcAdminClient.addUserToSpace(
                AddUserToSpaceRequest.newBuilder()
                    .setSpaceId(spaceId)
                    .setUserId(userId.toString())
                    .setRole(UserSpaceRole.MEMBER.name)
                    .build()
            )
        } catch (ex: StatusRuntimeException) {
            logger.error(ex) { "Error during adding new user=$userId to space=$spaceId with role=${UserSpaceRole.MEMBER.name}" }
            throw ex.toApiException()
        }
    }

    override fun tryToDeleteUserFromSpaceInAuthz(spaceId: Long, userId: UUID) {
        try {
            grpcAdminClient.removeUserFromSpace(
                RemoveUserFromSpaceRequest.newBuilder()
                    .setSpaceId(spaceId)
                    .setUserId(userId.toString())
                    .build()
            )
        } catch (ex: StatusRuntimeException) {
            logger.error(ex) { "Error during deleting user=$userId from space=$spaceId" }
            throw ex.toApiException()
        }
    }

    override fun tryToDeleteSpaceInAuthz(spaceId: Long) {
        try {
            grpcAdminClient.deleteSpace(
                DeleteSpaceRequest.newBuilder()
                    .setSpaceId(spaceId)
                    .build()
            )
        } catch (ex: StatusRuntimeException) {
            logger.error(ex) { "Error during deleting space $spaceId" }
            throw ex.toApiException()
        }
    }

    override fun checkAccess(spaceId: Long, action: AuthorizationAction): Boolean {
        return grpcBasicClient.authorize(
            AuthorizeRequest.newBuilder()
                .setKind("space")
                .setResourceId(spaceId)
                .setAction(action.actionName)
                .build()
        ).allowed
    }

}