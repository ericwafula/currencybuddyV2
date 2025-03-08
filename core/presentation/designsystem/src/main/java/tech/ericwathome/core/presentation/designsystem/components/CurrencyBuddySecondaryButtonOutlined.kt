package tech.ericwathome.core.presentation.designsystem.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import tech.ericwathome.core.presentation.designsystem.CurrencybuddyTheme
import tech.ericwathome.core.presentation.designsystem.utils.LocalTextUtils
import tech.ericwathome.core.presentation.designsystem.utils.PreviewLightDarkWithBackground

@Composable
fun CurrencyBuddySecondaryButtonOutlined(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
) {
    val textUtils = LocalTextUtils.current

    Box(
        modifier =
            modifier
                .sizeIn(minHeight = 56.dp, minWidth = 56.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(12.dp),
                )
                .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style =
                MaterialTheme.typography.bodyLarge.copy(
                    fontSize = with(textUtils) { MaterialTheme.typography.bodyLarge.fontSize.fixedSize },
                ),
        )
    }
}

@PreviewLightDarkWithBackground
@Composable
private fun SecondaryButtonOutlinedPreview() {
    CurrencybuddyTheme {
        Surface {
            CurrencyBuddySecondaryButtonOutlined(
                modifier = Modifier,
                onClick = { },
                text = "AC",
            )
        }
    }
}