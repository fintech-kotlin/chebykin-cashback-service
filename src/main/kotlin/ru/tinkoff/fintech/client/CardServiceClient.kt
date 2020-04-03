package ru.tinkoff.fintech.client

import ru.tinkoff.fintech.model.Card

interface CardServiceClient {

    suspend fun getCard(id: String): Card
}