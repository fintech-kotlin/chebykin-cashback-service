package ru.tinkoff.fintech.service.notification

import ru.tinkoff.fintech.model.NotificationMessageInfo

class NotificationMessageGeneratorImpl(
    private val cardNumberMasker: CardNumberMasker
) : NotificationMessageGenerator {

    override fun generateMessage(notificationMessageInfo: NotificationMessageInfo): String =
        //un poco de sexismo todo добавить бы признак пола для обращения (Уважаемый Оксана может не понять)
        """
           Уважаемый, ${notificationMessageInfo.name}!
           Спешим Вам сообщить, что на карту ${cardNumberMasker.mask(notificationMessageInfo.cardNumber)}
           начислен cashback в размере ${notificationMessageInfo.cashback}
           за категорию ${notificationMessageInfo.category}.
           Спасибо за покупку ${notificationMessageInfo.transactionDate}
        """.trimIndent()
}