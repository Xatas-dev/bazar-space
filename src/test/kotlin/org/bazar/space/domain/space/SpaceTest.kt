package org.bazar.space.domain.space

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import java.util.Base64

class SpaceTest {

    @Test
    fun `create trims the name`() {
        assertEquals("DOTA2", Space.create("  DOTA2  ").name)
    }

    @Test
    fun `create throws when name is blank`() {
        assertThrows(IllegalArgumentException::class.java) { Space.create("   ") }
    }

    @Test
    fun `create throws when name exceeds 255 characters`() {
        assertThrows(IllegalArgumentException::class.java) { Space.create("a".repeat(256)) }
    }

    @Test
    fun `create accepts name of exactly 255 characters`() {
        assertEquals(255, Space.create("a".repeat(255)).name.length)
    }

    @Test
    fun `rename returns a copy with the new name and same id`() {
        val space = Space.create("Old Name").copy(id = 42L)

        val renamed = space.rename("  New Name  ")

        assertEquals(42L, renamed.id)
        assertEquals("New Name", renamed.name)
    }

    @Test
    fun `rename throws when name is blank`() {
        assertThrows(IllegalArgumentException::class.java) { Space.create("Old").rename("  ") }
    }

    @Test
    fun `create generates join code of 16 random bytes`() {
        val joinCode = Space.create("DOTA2").joinCode

        assertEquals(16, Base64.getUrlDecoder().decode(joinCode).size)
    }

    @Test
    fun `create generates distinct join codes for different spaces`() {
        assertNotEquals(Space.create("DOTA2").joinCode, Space.create("DOTA2").joinCode)
    }

    @Test
    fun `rename keeps join code`() {
        val space = Space.create("Old")

        assertEquals(space.joinCode, space.rename("New").joinCode)
    }
}
