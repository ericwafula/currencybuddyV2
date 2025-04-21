package tech.ericwathome.core.presentation.ui

import android.content.Context
import android.graphics.drawable.Drawable
import java.io.File

object ImageUtil {
    fun getDrawableFromUri(
        context: Context,
        uri: String,
    ): Drawable? {
        val file = File(uri)
        return Drawable.createFromPath(file.absolutePath)
    }
}