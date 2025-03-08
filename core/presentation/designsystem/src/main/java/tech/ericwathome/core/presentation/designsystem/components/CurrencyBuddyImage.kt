@file:Keep

package tech.ericwathome.core.presentation.designsystem.components

import androidx.annotation.Keep
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import tech.ericwathome.core.presentation.designsystem.CurrencybuddyTheme
import tech.ericwathome.core.presentation.designsystem.R
import tech.ericwathome.core.presentation.designsystem.assets.FrownIcon
import tech.ericwathome.core.presentation.designsystem.utils.ImageUtils
import tech.ericwathome.core.presentation.designsystem.utils.shimmerEffect

@Composable
fun CurrencyBuddyImage(
    modifier: Modifier = Modifier,
    imageUrl: String,
    imageType: ImageUtils.ImageType,
    contentDescription: String,
) {
    SubcomposeAsyncImage(
        modifier = modifier,
        model = ImageUtils.createImageRequest(imageType, imageUrl),
        contentDescription = stringResource(R.string.image),
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
                    contentDescription = contentDescription,
                    tint = MaterialTheme.colorScheme.error,
                )
            }
        },
    )
}

@Preview
@Composable
private fun CurrencyBuddyImagePreview() {
    CurrencybuddyTheme {
        Surface {
            Box(modifier = Modifier.padding(16.dp)) {
                CurrencyBuddyImage(
                    modifier = Modifier.size(32.dp),
                    imageUrl = "https://example.com/image.png",
                    imageType = ImageUtils.ImageType.PNG,
                    contentDescription = stringResource(R.string.image),
                )
            }
        }
    }
}