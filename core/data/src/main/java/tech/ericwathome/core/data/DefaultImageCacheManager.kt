package tech.ericwathome.core.data

import android.content.Context
import androidx.core.content.FileProvider
import io.ktor.client.HttpClient
import tech.ericwathome.core.data.network.get
import tech.ericwathome.core.domain.ImageCacheManager
import tech.ericwathome.core.domain.ImageUri
import tech.ericwathome.core.domain.util.DataError
import tech.ericwathome.core.domain.util.Result
import java.io.File
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class DefaultImageCacheManager(
    private val context: Context,
    private val httpClient: HttpClient
) : ImageCacheManager {
    override suspend fun cacheImage(url: String, imageType: ImageCacheManager.ImageType): Result<ImageUri, DataError.Network> {
        val result = httpClient.get<ByteArray>(url)

        if (result is Result.Success) {
            val file = File(context.cacheDir, createFileName(imageType))
            file.outputStream().use { it.write(result.data) }

            val imageUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            return Result.Success(imageUri.toString())
        }

        return result as Result.Error
    }

    private fun createFileName(imageType: ImageCacheManager.ImageType): String {
        val dtf = DateTimeFormatter.ofPattern("ddMMyyyy_HHmmss_SSS")
        val currentTime = LocalDateTime.now(ZoneOffset.UTC)

        return when (imageType) {
            ImageCacheManager.ImageType.PNG -> "${dtf.format(currentTime)}.png"
            ImageCacheManager.ImageType.JPG -> "${dtf.format(currentTime)}.jpg"
            ImageCacheManager.ImageType.SVG -> "${dtf.format(currentTime)}.svg"
            ImageCacheManager.ImageType.WEBP -> "${dtf.format(currentTime)}.webp"
        }
    }
}