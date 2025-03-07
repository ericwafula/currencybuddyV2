package tech.ericwathome.converter.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import tech.ericwathome.converter.presentation.R
import tech.ericwathome.core.presentation.designsystem.CurrencybuddyTheme
import tech.ericwathome.core.presentation.designsystem.assets.DownIconThin
import tech.ericwathome.core.presentation.designsystem.components.CurrencyBuddyImage
import tech.ericwathome.core.presentation.designsystem.utils.ImageUtils
import tech.ericwathome.core.presentation.designsystem.utils.PreviewLightDarkWithBackground

@Composable
fun CurrencyPickerButton(
    modifier: Modifier = Modifier,
    imageUrl: String,
    text: String,
    onClick: () -> Unit,
) {
    Row(
        modifier =
            modifier
                .clip(RoundedCornerShape(12.dp))
                .clickable { onClick() }
                .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CurrencyBuddyImage(
            Modifier
                .size(32.dp)
                .clip(CircleShape),
            imageUrl = imageUrl,
            imageType = ImageUtils.ImageType.SVG,
            contentDescription = stringResource(id = R.string.flag_image),
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            style =
                MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSecondary,
                ),
        )
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            imageVector = DownIconThin,
            contentDescription = stringResource(R.string.drop_down_icon),
            tint = MaterialTheme.colorScheme.onSecondary,
        )
    }
}

@PreviewLightDarkWithBackground
@Composable
private fun CurrencyPickerButtonPreview() {
    CurrencybuddyTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.secondary)) {
            CurrencyPickerButton(
                modifier = Modifier,
                imageUrl = "",
                text = "EUR",
                onClick = { },
            )
        }
    }
}