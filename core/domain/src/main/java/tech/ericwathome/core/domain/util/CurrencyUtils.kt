package tech.ericwathome.core.domain.util

import java.util.Locale

object CurrencyUtils {
    fun Double.roundToDecimalPlaces(decimalPlaces: Int): Double {
        return String.format(Locale.getDefault(), "%.${decimalPlaces}f", this).toDouble()
    }
}