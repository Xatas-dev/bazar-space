package org.bazar.space.domain.space

import org.bazar.space.domain.exception.DomainException
import org.bazar.space.domain.userspace.UserSpace
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import java.util.UUID

class SpaceMembershipTest {

    private val ownerId = UUID.randomUUID()
    private val memberId = UUID.randomUUID()
    private val outsiderId = UUID.randomUUID()

    private val members = listOf(
        UserSpace.createOwner(spaceId = 1L, userId = ownerId),
        UserSpace.createMember(spaceId = 1L, userId = memberId)
    )

    @Test
    fun `ensureUserIsMember passes when user is a member`() {
        assertDoesNotThrow { SpaceMembership.ensureUserIsMember(members, memberId) }
    }

    @Test
    fun `ensureUserIsMember passes when user is the owner`() {
        assertDoesNotThrow { SpaceMembership.ensureUserIsMember(members, ownerId) }
    }

    @Test
    fun `ensureUserIsMember throws when user is not a member`() {
        assertThrows(DomainException::class.java) {
            SpaceMembership.ensureUserIsMember(members, outsiderId)
        }
    }

    @Test
    fun `ensureUserIsMember throws when space has no members`() {
        assertThrows(DomainException::class.java) {
            SpaceMembership.ensureUserIsMember(emptyList(), outsiderId)
        }
    }

    @Test
    fun `isCreator returns true only for the creator`() {
        assertTrue(SpaceMembership.isCreator(members, ownerId))
        assertFalse(SpaceMembership.isCreator(members, memberId))
        assertFalse(SpaceMembership.isCreator(members, outsiderId))
    }
}
