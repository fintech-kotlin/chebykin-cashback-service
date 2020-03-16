package ru.tinkoff.fintech.service.cashback

import org.springframework.stereotype.Service
import ru.tinkoff.fintech.model.TransactionInfo
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Month
import java.time.format.TextStyle
import java.util.*

internal const val LOYALTY_PROGRAM_BLACK = "BLACK"
internal const val LOYALTY_PROGRAM_ALL = "ALL"
internal const val LOYALTY_PROGRAM_BEER = "BEER"
internal const val MAX_CASH_BACK = 3000.0
internal const val MCC_SOFTWARE = 5734
internal const val MCC_BEER = 5921

typealias CalcCashbackPercentClassic = (TransactionInfo) -> Double

@Service
class CashbackCalculatorImpl : CashbackCalculator {
    companion object {
        private val BLACK_CASHBACK_PERCENT = 1.0
        private val SIX_SIX_SIX_CASHBACK_PERCENT = 6.66

        private val PRIBILEGED_NAME = "Олег"
        private val PRIBILEGED_LASTNAME = "Олегов"
    }

    private val calcIf666Cashback: CalcCashbackPercentClassic = { trInfo ->
        if (isDivided(trInfo.transactionSum, 666.0)) SIX_SIX_SIX_CASHBACK_PERCENT
        else 0.0
    }

    private val calcIfAllCashback: CalcCashbackPercentClassic = { trInfo ->
        lcm(trInfo.firstName.length, trInfo.lastName.length).toDouble() / 1000.0
    }

    private val calcIfBeerCashback: CalcCashbackPercentClassic = { trInfo ->
        when (true) {
            trInfo.firstName.equals(PRIBILEGED_NAME, true) &&
                    trInfo.lastName.equals(PRIBILEGED_LASTNAME, true) -> 10.0
            trInfo.firstName.equals(PRIBILEGED_NAME, true) -> 7.0
            isEqualFirstSymbolsOfMounthAndName(LocalDate.now().month, trInfo.firstName) -> 5.0
            isEqualFirstSymbolsOfMounthAndName(LocalDate.now().month.plus(1), trInfo.firstName),
            isEqualFirstSymbolsOfMounthAndName(
                LocalDate.now().month.minus(1),
                trInfo.firstName
            ) -> 3.0

            else -> 2.0
        }
    }

    override fun calculateCashback(transactionInfo: TransactionInfo): Double {
        val result =
            (transactionInfo.transactionSum * calculateCashbackPercent(transactionInfo) +
                    calcIf666Cashback(transactionInfo))
                .roundTo2Decimal()
        return if (result + transactionInfo.cashbackTotalValue <= MAX_CASH_BACK) result
        else MAX_CASH_BACK - transactionInfo.cashbackTotalValue
    }

    private fun calculateCashbackPercent(transactionInfo: TransactionInfo): Double {
        val resultPercent = when (transactionInfo.loyaltyProgramName) {
            LOYALTY_PROGRAM_BLACK -> BLACK_CASHBACK_PERCENT
            LOYALTY_PROGRAM_ALL -> getAllLoyalityProgramCashback(transactionInfo)
            LOYALTY_PROGRAM_BEER -> getBeerLoyalityProgramCashback(transactionInfo)
            else -> 0.0
        }
        return resultPercent / 100
    }

    private fun getBeerLoyalityProgramCashback(transactionInfo: TransactionInfo) =
        transactionInfo.mccCode?.let {
            if (it == MCC_BEER) {
                calcIfBeerCashback(transactionInfo)
            } else {
                0.0
            }
        } ?: 0.0

    private fun getAllLoyalityProgramCashback(transactionInfo: TransactionInfo) =
        transactionInfo.mccCode?.let {
            if (it == MCC_SOFTWARE) {
                if (isPalindromWithPossibleMistakes(
                        sum = (transactionInfo.transactionSum * 100).toInt()
                    )
                ) {
                    calcIfAllCashback(transactionInfo)
                } else {
                    0.0
                }
            } else {
                0.0
            }
        } ?: 0.0


    private fun isEqualFirstSymbolsOfMounthAndName(month: Month, name: String): Boolean =
        month.getDisplayName(TextStyle.FULL, Locale("ru"))[0].equals(name[0], true)


    //Методы ниже можно вынести в отдельный утилитный файл
    private fun Double.roundTo2Decimal(): Double =
        BigDecimal(this).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()

    private fun isDivided(x: Double, y: Double) = x % y == 0.0

    private fun lcm(a: Int, b: Int): Int {
        return a / gcd(a, b) * b
    }

    private fun gcd(a: Int, b: Int): Int {
        return if (b == 0) a else gcd(b, a % b)
    }

    private fun isPalindromWithPossibleMistakes(sum: Int, countPossibleMistakes: Int = 1): Boolean {
        var mistakesCount = 0

        var sumToAnalysis = sum
        var reversedCheckedDigit: Int
        var lastCheckedDigit: Int
        var iteration = 0
        while (sumToAnalysis != 0 &&
            sumToAnalysis / 10 > 0
        ) {
            lastCheckedDigit = sumToAnalysis % 10
            reversedCheckedDigit = sum.toString().substring(iteration, iteration + 1).toInt()

            if (reversedCheckedDigit != lastCheckedDigit) {
                mistakesCount++
            }
            if (mistakesCount > countPossibleMistakes) {
                return false
            }
            sumToAnalysis /= 10
            iteration++
        }
        return true
    }

}