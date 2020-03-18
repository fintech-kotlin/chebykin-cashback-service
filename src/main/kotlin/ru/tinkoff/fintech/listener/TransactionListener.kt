package ru.tinkoff.fintech.listener

import mu.KLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import ru.tinkoff.fintech.model.Transaction
import ru.tinkoff.fintech.service.processor.TransactionProcessor

@Component
class TransactionListener(private val processor: TransactionProcessor) {

    companion object : KLogging()

    @KafkaListener(topics = ["\${spring.kafka.consumer.topic}"])
    fun onMessage(transaction: Transaction) {
        logger.debug("send transaction $transaction to Processor")
        processor.processTransaction(transaction)
    }
}


