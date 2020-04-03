package ru.tinkoff.fintech.service.cashback

import org.springframework.stereotype.Service
import ru.tinkoff.fintech.dsl.Rule
import ru.tinkoff.fintech.model.TransactionInfo
import ru.tinkoff.fintech.service.cashback.config.ConfigCashbackRules


@Service
class CashbackRuleMatcher {
    private val rules = ConfigCashbackRules().rules

    fun match(transactionInfo: TransactionInfo): Rule {
        var bestMatchRule = Rule()

        for (rule in rules) {
            if(rule.matchTransaction(transactionInfo) && rule.weight > bestMatchRule.weight) {
                bestMatchRule = rule
            }
        }
        return bestMatchRule
    }
}