package ru.tinkoff.fintech.configs

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import java.time.Duration

@Configuration
class RestTemplateConfiguration {

    @Bean
    fun getRestTemplate():RestTemplate = RestTemplateBuilder()
        .setConnectTimeout(Duration.ofMillis(10000L))
        .setReadTimeout(Duration.ofMillis(10000L))
        .build()
}