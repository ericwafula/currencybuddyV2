package tech.ericwathome.widget.presentation

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.core.content.FileProvider
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.coroutineScope
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream

internal suspend fun loadImageAndSaveToFile(
    context: Context,
    url: String,
    fileName: String,
): Uri? =
    coroutineScope {
        try {
            Timber.tag("CurrencyWidget").d("Downloading image from: $url")
            val imageLoader = createSvgImageLoader(context)

            val request = createImageRequest(context, url)

            val result = downloadBitmapDrawable(imageLoader, request)

            val file = File(context.cacheDir, fileName)
            if (result != null) {
                compressImage(file, result, fileName, 50)
            }

            if (!file.exists()) {
                Timber.tag("CurrencyWidget").e("File was not created: $file")
                return@coroutineScope null
            }

            val uri = getFileUri(context, file)

            Timber.tag("CurrencyWidget").d("Saved image URI: $uri")
            return@coroutineScope uri
        } catch (e: Exception) {
            Timber.tag("CurrencyWidget").e(e, "Exception while downloading/saving image")
            return@coroutineScope null
        }
    }

private suspend fun downloadBitmapDrawable(
    imageLoader: ImageLoader,
    request: ImageRequest,
) = coroutineScope {
    (imageLoader.execute(request) as? SuccessResult)?.drawable
        as? BitmapDrawable ?: run {
        Timber.tag("CurrencyWidget").e("Failed to download image")
        return@coroutineScope null
    }
}

private fun createImageRequest(
    context: Context,
    url: String,
) = ImageRequest.Builder(context)
    .data(url)
    .allowHardware(false)
    .build()

private fun createSvgImageLoader(context: Context) =
    ImageLoader.Builder(context)
        .components {
            add(SvgDecoder.Factory())
        }
        .build()

private fun getFileUri(
    context: Context,
    file: File,
): Uri? {
    val uri =
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file,
        )
    return uri
}

private fun compressImage(
    file: File,
    result: BitmapDrawable,
    fileName: String,
    quality: Int,
) {
    FileOutputStream(file).use {
        val success = result.bitmap.compress(Bitmap.CompressFormat.PNG, quality, it)
        if (!success) {
            Timber.tag("CurrencyWidget").e("Failed to compress bitmap for $fileName")
            return
        }
    }
}

internal fun clearOldFlagImages(context: Context) {
    context.cacheDir.listFiles { _, name ->
        name.startsWith("base_") || name.startsWith("quote_")
    }?.forEach { it.delete() }
}

fun Context.grantUriPermissionToHomeActivity(
    uri: Uri,
    flags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION,
) {
    val launcherPackages = packageManager.homeActivityPackages()
    launcherPackages.forEach { launcherPackage ->
        grantUriPermission(launcherPackage, uri, flags)
    }
}

fun PackageManager.homeActivityPackages(): List<String> {
    val intent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME)
    return queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        .map { it.activityInfo.packageName }
}