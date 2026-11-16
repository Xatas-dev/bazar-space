package org.bazar.space.application.space.port.out

interface ValidationAvatarPropertiesPort {

    fun getAllowedFormat(): List<String>

    fun getMaxSizeBytes(): Long

    fun getAllowedMimeTypes(): List<String>
}