package ru.tinkoff.fintech.service.processor

import ru.tinkoff.fintech.model.Transaction

interface TransactionProcessor {
    fun processTransaction(transaction: Transaction)
}