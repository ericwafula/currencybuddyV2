@file:Keep

package tech.ericwathome.core.presentation.designsystem.components

import androidx.annotation.Keep
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tech.ericwathome.core.presentation.designsystem.CurrencybuddyTheme
import tech.ericwathome.core.presentation.designsystem.utils.PreviewLightDarkWithBackground

@Composable
fun ActionRowLayout(
    modifier: Modifier = Modifier,
    onClickInputKey: (Char) -> Unit,
    ctaText: String,
    onClickCta: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        SecondaryButton(
            modifier = Modifier.width(80.dp),
            text = "0",
            onClick = { onClickInputKey('0') },
        )
        SecondaryButton(
            modifier = Modifier.width(80.dp),
            text = ".",
            onClick = { onClickInputKey('.') },
        )
        PrimaryButton(
            modifier = Modifier.weight(1f),
            text = ctaText,
            onClick = onClickCta,
        )
    }
}

@PreviewLightDarkWithBackground
@Composable
private fun ActionRowLayoutPreview() {
    CurrencybuddyTheme {
        Surface {
            ActionRowLayout(
                onClickInputKey = {},
                ctaText = "Convert",
                onClickCta = {},
            )
        }
    }
}