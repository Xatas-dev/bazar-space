package org.bazar.space.domain.space

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

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
}
