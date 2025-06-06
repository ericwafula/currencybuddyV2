@file:Keep

package tech.ericwathome.core.presentation.designsystem.components

import androidx.annotation.Keep
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import tech.ericwathome.core.presentation.designsystem.CurrencybuddyTheme
import tech.ericwathome.core.presentation.designsystem.utils.PreviewLightDarkWithBackground

@Composable
fun CurrencyBuddyPrimaryButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    fontSize: TextUnit = MaterialTheme.typography.bodyLarge.fontSize,
    enabled: Boolean = true,
) {
    Button(
        modifier = modifier.sizeIn(minHeight = 56.dp, minWidth = 168.dp),
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        enabled = enabled,
    ) {
        Text(
            text = text,
            style =
                MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = fontSize,
                ),
        )
    }
}

@PreviewLightDarkWithBackground
@Composable
private fun PrimaryButtonPreview() {
    CurrencybuddyTheme {
        Surface {
            CurrencyBuddyPrimaryButton(
                modifier = Modifier,
                onClick = { },
                text = "Get Started",
            )
        }
    }
}