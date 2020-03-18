package ru.tinkoff.fintech.client

import mu.KLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import ru.tinkoff.fintech.model.LoyaltyProgram
import java.lang.Exception

@Component
class LoyaltyServiceClientImpl(
    @Value("\${rest.services.uri.loyalty}") private val uri: String,
    private val restTemplate: RestTemplate
) : LoyaltyServiceClient {

    companion object : KLogging()

    override fun getLoyaltyProgram(id: String): LoyaltyProgram {

        val response = restTemplate.getForEntity("$uri/$id", LoyaltyProgram::class.java)

        if (response.statusCode.is2xxSuccessful) {
            return response.body ?: throw Exception(" response body is null")
        } else {
            val errorMessage = "Unsuccessful result. Status: ${response.statusCodeValue}"
            logger.error(errorMessage)
            throw RestClientException(errorMessage)
        }

    }
}