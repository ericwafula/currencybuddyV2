package tech.ericwathome.core.presentation.ui

import java.util.Locale
import kotlin.time.Duration

fun Duration.formatted(): String {
    val totalSeconds = inWholeSeconds
    val hours = String.format(Locale.getDefault(), "%02d", totalSeconds / (60 * 60))
    val minutes = String.format(Locale.getDefault(), "%02d", (totalSeconds % 3600) / 60)
    val seconds = String.format(Locale.getDefault(), "%02d", (totalSeconds % 60))

    return "$hours:$minutes:$seconds"
}