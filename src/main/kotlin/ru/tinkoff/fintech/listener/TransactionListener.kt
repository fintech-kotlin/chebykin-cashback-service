package ru.tinkoff.fintech.listener

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import ru.tinkoff.fintech.model.Transaction
import ru.tinkoff.fintech.service.processor.TransactionProcessor

@Service
class TransactionListener(val processor: TransactionProcessor) {

    @KafkaListener(topics = ["\${spring.kafka.consumer.topic}"])
    fun onMessage(message: String) {

        println(message)
        val objectMapper = ObjectMapper()
        val transaction = objectMapper.readValue<Transaction>(message, Transaction::class.java)
        processor.processTransaction(transaction)
    }
}


