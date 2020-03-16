package ru.tinkoff.fintech.service.notification

import org.springframework.stereotype.Service
import ru.tinkoff.fintech.client.NotificationServiceClient
import ru.tinkoff.fintech.model.NotificationMessageInfo

@Service
class NotificationServiceImpl(
    private val notificationServiceClient: NotificationServiceClient,
    private val messageGenerator: NotificationMessageGenerator
) : NotificationService {
    override fun sendNotification(clientId: String, notificationMessageInfo: NotificationMessageInfo) {
        val message = messageGenerator.generateMessage(notificationMessageInfo)

        notificationServiceClient.sendNotification(clientId, message)
    }
}