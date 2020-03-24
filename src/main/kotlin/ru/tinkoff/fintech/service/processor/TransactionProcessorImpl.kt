package ru.tinkoff.fintech.service.processor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mu.KLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.tinkoff.fintech.client.CardServiceClient
import ru.tinkoff.fintech.client.ClientService
import ru.tinkoff.fintech.client.LoyaltyServiceClient
import ru.tinkoff.fintech.db.entity.LoyaltyPaymentEntity
import ru.tinkoff.fintech.db.repository.LoyaltyPaymentRepository
import ru.tinkoff.fintech.model.NotificationMessageInfo
import ru.tinkoff.fintech.model.Transaction
import ru.tinkoff.fintech.model.TransactionInfo
import ru.tinkoff.fintech.service.cashback.CashbackCalculator
import ru.tinkoff.fintech.service.notification.NotificationService
import java.time.LocalDate

@Service
class TransactionProcessorImpl(
    private val cardService: CardServiceClient,
    private val clientService: ClientService,
    private val loyaltyService: LoyaltyServiceClient,
    private val loyaltyPaymentRepository: LoyaltyPaymentRepository,
    private val cashbackCalculator: CashbackCalculator,
    private val notificationService: NotificationService,
    @Value("\${rest.services.sign}") private val sign: String
) : TransactionProcessor {

    companion object : KLogging()

    override fun processTransaction(transaction: Transaction) {
        logger.debug("start process transation {}", transaction)

        runBlocking(Dispatchers.IO) {
            transaction.mccCode?.let {
                try {
                    val card = cardService.getCard(transaction.cardNumber)
                    val clientCallback = async { clientService.getClient(card.client) }
                    val loyaltyProgram = async { loyaltyService.getLoyaltyProgram(card.loyaltyProgram) }

                    val startOfMonth = LocalDate.from(transaction.time).withDayOfMonth(1).atStartOfDay()
                    val totalMonthSum = async {
                        loyaltyPaymentRepository.findByCardIdAndSignAndDateTimeAfter(card.id, sign, startOfMonth)
                            .fold(0.0, { acc, LoyaltyPaymentEntity -> acc + LoyaltyPaymentEntity.value })
                    }
                    val client = clientCallback.await()

                    val transactionInfo = TransactionInfo(
                        loyaltyProgramName = loyaltyProgram.await().name,
                        transactionSum = transaction.value,
                        mccCode = it,
                        firstName = client.firstName,
                        lastName = client.lastName,
                        middleName = client.middleName,
                        clientBirthDate = client.birthDate,
                        cashbackTotalValue = totalMonthSum.await()
                    )

                    val cashback = cashbackCalculator.calculateCashback(transactionInfo)

                    launch {
                        loyaltyPaymentRepository.save(
                            LoyaltyPaymentEntity(
                                value = cashback,
                                cardId = card.id,
                                sign = sign,
                                transactionId = transaction.transactionId,
                                dateTime = transaction.time
                            )
                        )
                    }

                    launch {
                        notificationService.sendNotification(
                            clientId = client.id,
                            notificationMessageInfo = buildMessage(transaction, transactionInfo, cashback)
                        )
                    }

                } catch (e: Exception) {
                    logger.error("Joder!", e)
                }
            } ?: logger.debug("Mcc is null. $transaction")
        }
    }

    private fun buildMessage(transaction: Transaction, transactionInfo: TransactionInfo, cashback: Double) =
        NotificationMessageInfo(
            transactionInfo.firstName,
            transaction.cardNumber,
            cashback,
            transactionInfo.transactionSum,
            transactionInfo.loyaltyProgramName,
            transaction.time
        )
}