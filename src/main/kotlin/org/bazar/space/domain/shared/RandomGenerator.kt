package org.bazar.space.domain.shared

import java.security.SecureRandom
import java.util.Base64

object RandomGenerator {
    private const val DEFAULT_TOKEN_BYTES = 16

    private val secureRandom = SecureRandom()
    private val base64Url = Base64.getUrlEncoder().withoutPadding()

    fun urlSafeToken(byteLength: Int = DEFAULT_TOKEN_BYTES): String {
        require(byteLength > 0) { "byteLength must be positive" }
        return ByteArray(byteLength).also(secureRandom::nextBytes).let(base64Url::encodeToString)
    }
}
