package tech.ericwathome.core.domain

import tech.ericwathome.core.domain.util.DataError
import tech.ericwathome.core.domain.util.Result

typealias ImageUri = String

interface ImageCacheManager {
    suspend fun cacheImage(url: String, imageType: ImageType): Result<ImageUri, DataError.Network>

    enum class ImageType {
        PNG,
        JPG,
        SVG,
        WEBP
    }
}