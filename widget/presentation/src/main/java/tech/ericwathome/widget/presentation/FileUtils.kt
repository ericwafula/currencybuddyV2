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
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream

internal suspend fun loadImageAndSaveToFile(
    context: Context,
    url: String,
    fileName: String,
): Uri? =
    withContext(Dispatchers.IO) {
        try {
            Timber.tag("CurrencyWidget").d("Downloading image from: $url")
            val imageLoader =
                ImageLoader.Builder(context)
                    .components {
                        add(SvgDecoder.Factory())
                    }
                    .build()

            val request =
                ImageRequest.Builder(context)
                    .data(url)
                    .allowHardware(false)
                    .build()

            val result =
                (imageLoader.execute(request) as? SuccessResult)?.drawable
                    as? BitmapDrawable ?: run {
                    Timber.tag("CurrencyWidget").e("Failed to download image")
                    return@withContext null
                }

            val file = File(context.cacheDir, fileName)
            FileOutputStream(file).use {
                val success = result.bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
                if (!success) {
                    Timber.tag("CurrencyWidget").e("Failed to compress bitmap for $fileName")
                    return@withContext null
                }
            }

            if (!file.exists()) {
                Timber.tag("CurrencyWidget").e("File was not created: $file")
                return@withContext null
            }

            val uri =
                FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    file,
                )

            Timber.tag("CurrencyWidget").d("Saved image URI: $uri")
            return@withContext uri
        } catch (e: Exception) {
            Timber.tag("CurrencyWidget").e(e, "Exception while downloading/saving image")
            return@withContext null
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