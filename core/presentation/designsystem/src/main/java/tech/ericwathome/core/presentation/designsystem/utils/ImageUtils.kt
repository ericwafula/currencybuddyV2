package tech.ericwathome.core.presentation.designsystem.utils

import androidx.annotation.Keep
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import coil.decode.SvgDecoder
import coil.request.ImageRequest

@Keep
object ImageUtils {
    @Keep
    enum class ImageType {
        PNG,
        JPG,
        SVG,
    }

    @Composable
    fun createImageRequest(
        imageType: ImageType,
        imageUrl: String,
    ): ImageRequest {
        val context = LocalContext.current

        return when (imageType) {
            ImageType.SVG -> {
                ImageRequest.Builder(
                    context = context,
                    request =
                        ImageRequest.Builder(context)
                            .data(imageUrl)
                            .decoderFactory(SvgDecoder.Factory())
                            .build(),
                ).build()
            }
            else ->
                ImageRequest.Builder(
                    context = context,
                    request =
                        ImageRequest.Builder(context)
                            .data(imageUrl)
                            .build(),
                ).build()
        }
    }
}