package ru.tinkoff.fintech.listener

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import ru.tinkoff.fintech.model.Transaction
import ru.tinkoff.fintech.service.processor.TransactionProcessor

@Component
class TransactionListener(val processor: TransactionProcessor) {


    @KafkaListener(topics = ["\${transactionProcessor.kafka.topic}"])
    fun onMessage(transaction: Transaction) {
        processor.processTransaction(transaction)
    }
}


