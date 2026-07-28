package org.bazar.space.infrastructure.persistence.entity

enum class OutboxStatus {
    NEW, IN_PROGRESS, DONE, ERROR
}