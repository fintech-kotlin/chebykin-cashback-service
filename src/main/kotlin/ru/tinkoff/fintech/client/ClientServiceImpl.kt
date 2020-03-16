package ru.tinkoff.fintech.client

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import ru.tinkoff.fintech.model.Client

@Component
class ClientServiceImpl(
    @Value("\${rest.services.uri.client}") private val uri: String,
    private val restTemplate: RestTemplate
) : ClientService {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(ClientServiceImpl::class.java)
    }

    override fun getClient(id: String): Client {
        val response = restTemplate.getForEntity("$uri/$id", Client::class.java)

        if (response.statusCode.is2xxSuccessful) {
            return response.body!!
        } else {
            val errorMessage = "Unsuccessful result. Status: ${response.statusCodeValue}"
            LOGGER.error(errorMessage)
            throw RestClientException(errorMessage)
        }

    }
}