package ru.tinkoff.fintech.dsl

import ru.tinkoff.fintech.model.TransactionInfo

typealias Condition = (TransactionInfo) -> Boolean
typealias ResultCashbackPercent = (TransactionInfo) -> Double

class Rule(private val name: String = "") {

    private val conditions: MutableList<Condition> = emptyList<Condition>().toMutableList()
    var resultCashback: ResultCashbackPercent = { 0.0 }
        set(value) {
            field = value
        }

    var weight = conditions.size
        get() = conditions.size
        private set

    fun addCondition(condition: Condition) = conditions.add(condition)

    fun matchTransaction(transactionInfo: TransactionInfo): Boolean = conditions.all { it(transactionInfo) }

    fun calculateCashbackPercent(transactionInfo: TransactionInfo) = resultCashback(transactionInfo)
}