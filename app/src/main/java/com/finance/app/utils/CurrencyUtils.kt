package com.finance.app.utils

import java.text.NumberFormat
import java.util.Locale

object CurrencyUtils {
    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US)

    fun formatAmount(amount: Double): String = currencyFormat.format(amount)

    fun formatAmountWithSign(amount: Double, isIncome: Boolean): String {
        val formatted = currencyFormat.format(amount)
        return if (isIncome) "+$formatted" else "-$formatted"
    }
}
