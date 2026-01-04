package org.bazar.space.service

import org.bazar.space.config.authorization.client.AuthorizationGrpcClient
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class SpaceManager(
    private val spaceService: SpaceService,
    private val userSpaceService: UserSpaceService,
    private val authorizationGrpcClient: AuthorizationGrpcClient
) {

    @Transactional
    fun createSpace(userId: UUID): Long {
        val spaceId = spaceService.createSpace()
        userSpaceService.addUserToSpace(spaceId, userId)
        authorizationGrpcClient.tryToCreateSpaceInAuthz(spaceId)
        return spaceId
    }

    @Transactional
    fun getAllUserSpaces(authenticatedUserId: UUID) = spaceService.getAllUserSpaces(authenticatedUserId)

    @Transactional
    fun getAllUsersInSpace(spaceId: Long) = userSpaceService.getAllUsersInSpace(spaceId)

    @Transactional
    fun addUserToSpace(userToAddId: UUID, spaceId: Long) {
        userSpaceService.addUserToSpace(spaceId, userToAddId)
        authorizationGrpcClient.tryToAddUserToSpaceInAuthz(spaceId, userToAddId)
    }

    @Transactional
    fun deleteUserFromSpace(spaceId: Long, userId: UUID){
        userSpaceService.deleteUserFromSpace(spaceId, userId)
        authorizationGrpcClient.tryToDeleteUserFromSpaceInAuthz(spaceId, userId)
    }

    @Transactional
    fun deleteSpace(spaceId: Long) {
        userSpaceService.deleteAllBySpaceId(spaceId)
        spaceService.deleteSpaceById(spaceId)
        authorizationGrpcClient.tryToDeleteSpaceInAuthz(spaceId)
    }

}