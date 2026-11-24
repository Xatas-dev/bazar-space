package org.bazar.space.domain.shared

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.Base64

class RandomGeneratorTest {

    @Test
    fun `urlSafeToken by default encodes 16 bytes as 22-char base64url without padding`() {
        val token = RandomGenerator.urlSafeToken()

        assertEquals(22, token.length)
        assertTrue(token.matches(Regex("^[A-Za-z0-9_-]+$")))
        assertEquals(16, Base64.getUrlDecoder().decode(token).size)
    }

    @Test
    fun `urlSafeToken respects byte length`() {
        assertEquals(32, Base64.getUrlDecoder().decode(RandomGenerator.urlSafeToken(32)).size)
    }

    @Test
    fun `urlSafeToken returns distinct values`() {
        val tokens = (1..1000).map { RandomGenerator.urlSafeToken() }.toSet()

        assertEquals(1000, tokens.size)
    }

    @Test
    fun `urlSafeToken rejects non-positive length`() {
        assertThrows(IllegalArgumentException::class.java) { RandomGenerator.urlSafeToken(0) }
    }
}
