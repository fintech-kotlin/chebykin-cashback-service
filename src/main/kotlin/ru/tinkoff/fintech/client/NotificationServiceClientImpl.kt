package ru.tinkoff.fintech.client

import mu.KLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate

@Component
class NotificationServiceClientImpl(
    @Value("\${rest.services.uri.notification}") private val uri: String,
    private val restTemplate: RestTemplate
) : NotificationServiceClient {

    companion object : KLogging()

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
            logger.error(errorMessage)
            throw RestClientException(errorMessage)
        }
    }
}