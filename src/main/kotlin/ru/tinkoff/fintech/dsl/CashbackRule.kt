package ru.tinkoff.fintech.dsl

class CashbackRule {

    companion object {
        fun rule(name: String, content: CashbackRuleBuilder.() -> Unit):Rule{
            val builder = CashbackRuleBuilder(name)
            builder.content()
            return builder.result
        }
    }

}