package ru.tinkoff.fintech.service.cashback.config

import java.time.Month
import java.time.format.TextStyle
import java.util.*

fun isPalindromWithPossibleMistakes(sum: Int, countPossibleMistakes: Int = 1): Boolean {
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

fun isEqualFirstSymbolsOfMounthAndName(month: Month, name: String): Boolean =
    month.getDisplayName(TextStyle.FULL, Locale("ru"))[0].equals(name[0], true)


fun lcm(a: Int, b: Int): Int {
    return a / gcd(a, b) * b
}

fun gcd(a: Int, b: Int): Int {
    return if (b == 0) a else gcd(b, a % b)
}