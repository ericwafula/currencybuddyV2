package tech.ericwathome.core.domain.util

import java.text.NumberFormat
import java.util.Locale

object CurrencyUtils {
    fun Double.roundToDecimalPlaces(decimalPlaces: Int): Double {
        return String.format(Locale.getDefault(), "%.${decimalPlaces}f", this).toDouble()
    }

    fun Double.formatCurrency(locale: Locale = Locale.getDefault()): String {
        return NumberFormat.getCurrencyInstance(locale).format(this)
    }
}