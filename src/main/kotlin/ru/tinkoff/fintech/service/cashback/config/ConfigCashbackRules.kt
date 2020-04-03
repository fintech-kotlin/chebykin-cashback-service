package ru.tinkoff.fintech.service.cashback.config

import ru.tinkoff.fintech.dsl.CashbackRule.Companion.rule
import ru.tinkoff.fintech.dsl.Rule
import java.time.LocalDate


internal const val LOYALTY_PROGRAM_BLACK = "BLACK"
internal const val LOYALTY_PROGRAM_ALL = "ALL"
internal const val LOYALTY_PROGRAM_BEER = "BEER"

internal const val MCC_SOFTWARE = 5734
internal const val MCC_BEER = 5921

internal const val PRIVILEGED_NAME = "Олег"
internal const val PRIVILEGED_LASTNAME = "Олегов"

class ConfigCashbackRules {
    val rules: MutableList<Rule> = emptyList<Rule>().toMutableList()

    init {
        rules.add(
            rule(name = "BLACK") {
                all({ info -> info.loyaltyProgramName == LOYALTY_PROGRAM_BLACK })
                then { 1.0 }
            })
        rules.add(
            rule(name = "ALL") {
                all(
                    { info -> info.loyaltyProgramName == LOYALTY_PROGRAM_ALL },
                    { info -> info.mccCode == MCC_SOFTWARE },
                    { info -> isPalindromWithPossibleMistakes((info.transactionSum * 100).toInt()) }
                )
                then { trInfo ->
                    lcm(trInfo.firstName.length, trInfo.lastName.length).toDouble() / 1000.0
                }
            })
        rules.add(
            rule(name = "BEER_FULL_PRIVELEGED_NAME") {
                all(
                    { info -> info.loyaltyProgramName == LOYALTY_PROGRAM_BEER },
                    { info -> info.mccCode == MCC_BEER },
                    { info -> info.firstName.equals(PRIVILEGED_NAME, true) },
                    { info -> info.lastName.equals(PRIVILEGED_LASTNAME, true) }
                )
                then { 10.0 }
            })
        rules.add(
            rule(name = "BEER_PRIVELEGED_NAME") {
                all(
                    { info -> info.loyaltyProgramName == LOYALTY_PROGRAM_BEER },
                    { info -> info.mccCode == MCC_BEER },
                    { info -> info.firstName.equals(PRIVILEGED_NAME, true) }
                )
                then { 7.0 }
            })
        rules.add(
            rule(name = "BEER_PRIVELEGED_NAME") {
                all(
                    { info -> info.loyaltyProgramName == LOYALTY_PROGRAM_BEER },
                    { info -> info.mccCode == MCC_BEER },
                    { info -> isEqualFirstSymbolsOfMounthAndName(LocalDate.now().month, info.firstName) }
                )
                then { 5.0 }
            })
        rules.add(
            rule(name = "BEER_PRIVELEGED_NAME") {
                all(
                    { info -> info.loyaltyProgramName == LOYALTY_PROGRAM_BEER },
                    { info -> info.mccCode == MCC_BEER },
                    { info -> isEqualFirstSymbolsOfMounthAndName(LocalDate.now().month.plus(1), info.firstName) ||
                            isEqualFirstSymbolsOfMounthAndName(LocalDate.now().month.minus(1), info.firstName)}
                )
                then { 3.0 }
            })
        rules.add(
            rule(name = "BEER") {
                all(
                    { info -> info.loyaltyProgramName == LOYALTY_PROGRAM_BEER },
                    { info -> info.mccCode == MCC_BEER }
                )
                then { 2.0 }
            })
        rules.add(
            rule(name = "NONE_OTHER_RULE_MATCHED")
            {
                all({ true })
                then { 0.0 }
            })
    }
}