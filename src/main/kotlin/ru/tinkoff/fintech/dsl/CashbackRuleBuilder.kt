package ru.tinkoff.fintech.dsl

class CashbackRuleBuilder(private val name: String) {
    internal var result = Rule(name)

    fun all(vararg conditions: Condition) {
        conditions.forEach { result.addCondition(it) }
    }

    fun then(resultPercent: ResultCashbackPercent) {
        result.resultCashback = resultPercent
    }
}