package ru.tinkoff.fintech.service.cashback

import org.springframework.stereotype.Service
import ru.tinkoff.fintech.model.TransactionInfo
import java.math.BigDecimal

internal const val LOYALTY_PROGRAM_BLACK = "BLACK"
internal const val LOYALTY_PROGRAM_ALL = "ALL"
internal const val LOYALTY_PROGRAM_BEER = "BEER"
internal const val MAX_CASH_BACK = 3000.0
internal const val MCC_SOFTWARE = 5734
internal const val MCC_BEER = 5921

typealias CalcCashbackPercentClassic = (TransactionInfo) -> Double

@Service
class CashbackCalculatorImpl(
    private val ruleMatcher: CashbackRuleMatcher
) : CashbackCalculator {

    // пока не придумал как правило относящееся к сумме, а не к проценту кешбека вынести в правила нормально [WIP]
    override fun calculateCashback(transactionInfo: TransactionInfo): Double {
        val result =
            (transactionInfo.transactionSum * calculateCashbackPercent(transactionInfo) +
                    if (isDivided(transactionInfo.transactionSum, 666.0)) 6.66
                    else 0.0)
                .roundTo2Decimal()
        return if (result + transactionInfo.cashbackTotalValue <= MAX_CASH_BACK) result
        else MAX_CASH_BACK - transactionInfo.cashbackTotalValue
    }

    private fun calculateCashbackPercent(transactionInfo: TransactionInfo): Double =
        ruleMatcher.match(transactionInfo).resultCashback(transactionInfo) / 100


    //Методы ниже можно вынести в отдельный утилитный файл
    private fun Double.roundTo2Decimal(): Double =
        BigDecimal(this).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()

    private fun isDivided(x: Double, y: Double) = x % y == 0.0

}