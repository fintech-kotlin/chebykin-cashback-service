package ru.tinkoff.fintech.client

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import ru.tinkoff.fintech.model.Card

@Component
class CardServiceClientImpl(
    @Value("\${rest.services.uri.card}") private val uri: String,
    private val restTemplate: RestTemplate
) : CardServiceClient {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(CardServiceClientImpl::class.java)
    }

    override fun getCard(id: String): Card {
        val response = restTemplate.getForEntity("$uri/$id", Card::class.java)

        if (response.statusCode.is2xxSuccessful) {
            return response.body!!
        } else {
            val errorMessage = "Unsuccessful result. Status: ${response.statusCodeValue}"
            LOGGER.error(errorMessage)
            throw RestClientException(errorMessage)
        }
    }
}