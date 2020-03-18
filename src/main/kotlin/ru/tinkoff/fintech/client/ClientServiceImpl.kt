package ru.tinkoff.fintech.client

import mu.KLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import ru.tinkoff.fintech.model.Client
import java.lang.Exception

@Component
class ClientServiceImpl(
    @Value("\${rest.services.uri.client}") private val uri: String,
    private val restTemplate: RestTemplate
) : ClientService {

    companion object : KLogging()

    override fun getClient(id: String): Client {
        val response = restTemplate.getForEntity("$uri/$id", Client::class.java)

        if (response.statusCode.is2xxSuccessful) {
            return response.body ?: throw Exception(" response body is null")
        } else {
            val errorMessage = "Unsuccessful result. Status: ${response.statusCodeValue}"
            logger.error(errorMessage)
            throw RestClientException(errorMessage)
        }

    }
}