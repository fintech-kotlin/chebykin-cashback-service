package ru.tinkoff.fintech.client

import mu.KLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import ru.tinkoff.fintech.model.Card
import java.lang.Exception

@Component
class CardServiceClientImpl(
    @Value("\${rest.services.uri.card}") private val uri: String,
    private val restTemplate: RestTemplate
) : CardServiceClient {

    companion object : KLogging()

    override fun getCard(id: String): Card {
        val response = restTemplate.getForEntity("$uri/$id", Card::class.java)

        if (response.statusCode.is2xxSuccessful) {
            return response.body ?: throw Exception(" response body is null")
        } else {
            val errorMessage = "Unsuccessful result. Status: ${response.statusCodeValue}"
            logger.error(errorMessage)
            throw RestClientException(errorMessage)
        }
    }
}