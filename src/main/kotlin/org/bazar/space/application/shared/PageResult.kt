package org.bazar.space.application.shared

data class PageResult<T>(
    val items: List<T>,
    val page: Int,
    val size: Int,
    val total: Long
)
