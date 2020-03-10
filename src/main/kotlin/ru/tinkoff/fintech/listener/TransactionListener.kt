package ru.tinkoff.fintech.listener

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import ru.tinkoff.fintech.model.Transaction
import ru.tinkoff.fintech.service.processor.TransactionProcessor

@Service
class TransactionListener(val processor: TransactionProcessor) {

    @KafkaListener(topics = ["\${spring.kafka.consumer.topic}"])
    fun onMessage(transaction: Transaction) {
        processor.processTransaction(transaction)
    }
}


