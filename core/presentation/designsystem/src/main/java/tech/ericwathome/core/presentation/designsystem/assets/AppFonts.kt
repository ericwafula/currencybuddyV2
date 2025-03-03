package tech.ericwathome.core.presentation.designsystem.assets

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import tech.ericwathome.core.presentation.designsystem.R

internal object AppFonts {
    val inter: FontFamily = interFontFamily
}

internal val interFontFamily: FontFamily =
    FontFamily(
        listOf(
            Font(
                resId = R.font.inter,
                weight = FontWeight.Normal,
            ),
            Font(
                resId = R.font.inter_medium,
                weight = FontWeight.Normal,
            ),
            Font(
                resId = R.font.inter_semibold,
                weight = FontWeight.Normal,
            ),
            Font(
                resId = R.font.inter_bold,
                weight = FontWeight.Normal,
            ),
        ),
    )