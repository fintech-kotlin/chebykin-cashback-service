package ru.tinkoff.fintech.service.notification

import java.lang.Exception

class CardNumberMaskerImpl : CardNumberMasker {

    override fun mask(cardNumber: String, maskChar: Char, start: Int, end: Int): String {
        if (end < start) throw Exception("start: $start must be less or equal to end: $end, but it's not")
        var countFromEnd = cardNumber.length - end
        countFromEnd = if (countFromEnd < 0) 0 else countFromEnd
        return cardNumber.replace(Regex("(?<=.{$start}).(?=.{$countFromEnd})"), maskChar.toString())
    }
}