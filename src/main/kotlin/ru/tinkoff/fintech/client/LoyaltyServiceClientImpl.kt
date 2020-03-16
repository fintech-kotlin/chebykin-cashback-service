package ru.tinkoff.fintech.client

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import ru.tinkoff.fintech.model.LoyaltyProgram

@Component
class LoyaltyServiceClientImpl(
    @Value("\${rest.services.uri.loyalty}") private val uri: String,
    private val restTemplate: RestTemplate
) : LoyaltyServiceClient {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(LoyaltyServiceClientImpl::class.java)
    }

    override fun getLoyaltyProgram(id: String): LoyaltyProgram {

        val response = restTemplate.getForEntity("$uri/$id", LoyaltyProgram::class.java)

        if (response.statusCode.is2xxSuccessful) {
            return response.body!!
        } else {
            val errorMessage = "Unsuccessful result. Status: ${response.statusCodeValue}"
            LOGGER.error(errorMessage)
            throw RestClientException(errorMessage)
        }

    }
}