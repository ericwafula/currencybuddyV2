package tech.ericwathome.converter.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import tech.ericwathome.converter.presentation.R
import tech.ericwathome.core.presentation.designsystem.CurrencybuddyTheme
import tech.ericwathome.core.presentation.designsystem.assets.DownIconThin
import tech.ericwathome.core.presentation.designsystem.assets.FrownIcon
import tech.ericwathome.core.presentation.designsystem.utils.PreviewLightDarkWithBackground
import tech.ericwathome.core.presentation.ui.shimmerEffect

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
        SubcomposeAsyncImage(
            modifier =
                Modifier
                    .size(32.dp)
                    .clip(CircleShape),
            contentDescription = stringResource(id = R.string.flag_image),
            model =
                ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .decoderFactory(SvgDecoder.Factory())
                    .build(),
            contentScale = ContentScale.Crop,
            loading = {
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .shimmerEffect(),
                )
            },
            error = {
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.onSecondary),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = FrownIcon,
                        contentDescription = stringResource(R.string.error_icon),
                        tint = MaterialTheme.colorScheme.error,
                    )
                }
            },
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