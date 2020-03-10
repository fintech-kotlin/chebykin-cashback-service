package ru.tinkoff.fintech.service.processor

import ru.tinkoff.fintech.model.Transaction

class TransactionProcessorImpl(): TransactionProcessor {
    override fun processTransaction(transaction: Transaction) {
        println(transaction)
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}