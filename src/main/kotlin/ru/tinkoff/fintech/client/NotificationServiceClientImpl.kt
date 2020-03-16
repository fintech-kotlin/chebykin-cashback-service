package ru.tinkoff.fintech.client

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import ru.tinkoff.fintech.model.LoyaltyProgram

@Component
class NotificationServiceClientImpl(
    @Value("\${rest.services.uri.notification}") private val uri: String,
    private val restTemplate: RestTemplate
) : NotificationServiceClient {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(NotificationServiceClientImpl::class.java)
    }

    override fun sendNotification(clientId: String, message: String) {
        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.APPLICATION_JSON_UTF8

        val response = restTemplate.postForEntity(
            "$uri/$clientId/message",
            HttpEntity(message, httpHeaders),
            String::class.java
        )
        if (!response.statusCode.is2xxSuccessful) {
            val errorMessage = """
                Ahtung! PUT request failed
                Message: $message
                Unsuccessful result. Status: ${response.statusCodeValue}
            """.trimIndent()
            LOGGER.error(errorMessage)
            throw RestClientException(errorMessage)
        }
    }
}