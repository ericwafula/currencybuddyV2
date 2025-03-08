@file:Keep

package tech.ericwathome.core.presentation.designsystem.components

import androidx.annotation.Keep
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tech.ericwathome.core.presentation.designsystem.CurrencybuddyTheme
import tech.ericwathome.core.presentation.designsystem.utils.LocalTextUtils
import tech.ericwathome.core.presentation.designsystem.utils.PreviewLightDarkWithBackground

@Composable
fun CurrencyBuddyActionRowLayout(
    modifier: Modifier = Modifier,
    onClickInputKey: (Char) -> Unit,
    ctaText: String,
    onClickCta: () -> Unit,
) {
    val textUtils = LocalTextUtils.current

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        CurrencyBuddySecondaryButton(
            modifier = Modifier.width(80.dp),
            text = "0",
            onClick = { onClickInputKey('0') },
        )
        CurrencyBuddySecondaryButton(
            modifier = Modifier.width(80.dp),
            text = ".",
            onClick = { onClickInputKey('.') },
        )
        CurrencyBuddyPrimaryButton(
            modifier = Modifier.weight(1f),
            text = ctaText,
            onClick = onClickCta,
            fontSize = with(textUtils) { MaterialTheme.typography.bodyLarge.fontSize.fixedSize },
        )
    }
}

@PreviewLightDarkWithBackground
@Composable
private fun ActionRowLayoutPreview() {
    CurrencybuddyTheme {
        Surface {
            CurrencyBuddyActionRowLayout(
                onClickInputKey = {},
                ctaText = "Convert",
                onClickCta = {},
            )
        }
    }
}