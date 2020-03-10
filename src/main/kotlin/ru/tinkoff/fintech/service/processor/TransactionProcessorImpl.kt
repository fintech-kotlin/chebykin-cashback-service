package ru.tinkoff.fintech.service.processor

import org.springframework.stereotype.Service
import ru.tinkoff.fintech.model.Transaction

@Service
class TransactionProcessorImpl(): TransactionProcessor {
    override fun processTransaction(transaction: Transaction) {
        println("loasbdsjdbkakdbaksjdbakdjbasdbk" + transaction)
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}