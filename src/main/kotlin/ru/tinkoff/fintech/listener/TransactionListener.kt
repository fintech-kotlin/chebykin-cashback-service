package ru.tinkoff.fintech.listener

import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import ru.tinkoff.fintech.model.Transaction
import ru.tinkoff.fintech.service.processor.TransactionProcessor

@Component
class TransactionListener(private val processor: TransactionProcessor) {

    companion object {
        private val LOG = LoggerFactory.getLogger(TransactionListener::class.java)
    }

    @KafkaListener(topics = ["\${spring.kafka.consumer.topic}"])
    fun onMessage(transaction: Transaction) {
        LOG.warn("send transaction $transaction to Processor")
        processor.processTransaction(transaction)
    }
}


