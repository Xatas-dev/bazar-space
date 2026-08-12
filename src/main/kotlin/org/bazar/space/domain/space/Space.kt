package org.bazar.space.domain.space

import java.time.Instant

data class Space(
    val id: Long? = null,
    val name: String,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now()
) {

    fun rename(newName: String): Space = copy(name = validatedName(newName))

    companion object {
        fun create(name: String): Space = Space(name = validatedName(name))

        private fun validatedName(name: String): String {
            val trimmed = name.trim()
            require(trimmed.isNotBlank()) { "Space name must not be blank" }
            require(trimmed.length <= 255) { "Space name must not exceed 255 characters" }
            return trimmed
        }
    }
}
