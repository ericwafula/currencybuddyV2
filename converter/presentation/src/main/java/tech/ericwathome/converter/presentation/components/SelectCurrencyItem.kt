package tech.ericwathome.converter.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import tech.ericwathome.converter.presentation.R
import tech.ericwathome.core.presentation.designsystem.CurrencybuddyTheme
import tech.ericwathome.core.presentation.designsystem.components.CurrencyBuddyImage
import tech.ericwathome.core.presentation.designsystem.components.CurrencyBuddyRadioIcon
import tech.ericwathome.core.presentation.designsystem.utils.ImageUtils

@Composable
fun SelectCurrencyItem(
    modifier: Modifier = Modifier,
    imageUrl: String,
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier =
            modifier
                .clip(RoundedCornerShape(12.dp))
                .clickable { onClick() }
                .background(color = MaterialTheme.colorScheme.onSurface.copy(0.1f))
                .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                CurrencyBuddyImage(
                    modifier = Modifier.size(32.dp).clip(CircleShape),
                    imageUrl = imageUrl,
                    imageType = ImageUtils.ImageType.SVG,
                    contentDescription = stringResource(R.string.flag_image),
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            CurrencyBuddyRadioIcon(selected = selected)
        }
    }
}

@PreviewLightDark
@Composable
private fun SelectCurrencyItemPreview() {
    CurrencybuddyTheme {
        Surface {
            Box(modifier = Modifier.padding(16.dp)) {
                SelectCurrencyItem(
                    modifier = Modifier.fillMaxWidth(),
                    imageUrl = "https://restcountries.com/data/afg.svg",
                    text = "Euro(EUR)",
                    selected = true,
                    onClick = { },
                )
            }
        }
    }
}