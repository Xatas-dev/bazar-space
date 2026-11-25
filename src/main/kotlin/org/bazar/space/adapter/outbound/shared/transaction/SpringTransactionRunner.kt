package org.bazar.space.adapter.outbound.shared.transaction

import org.bazar.space.application.shared.port.out.TransactionRunner
import org.springframework.stereotype.Component
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.TransactionTemplate

@Component
class SpringTransactionRunner(transactionManager: PlatformTransactionManager) : TransactionRunner {

    private val transactionTemplate = TransactionTemplate(transactionManager)

    @Suppress("UNCHECKED_CAST")
    override fun <T> inTransaction(block: () -> T): T =
        transactionTemplate.execute { block() }
}
