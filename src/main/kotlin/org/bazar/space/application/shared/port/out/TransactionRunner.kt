package org.bazar.space.application.shared.port.out

interface TransactionRunner {
    fun <T> inTransaction(block: () -> T): T
}
