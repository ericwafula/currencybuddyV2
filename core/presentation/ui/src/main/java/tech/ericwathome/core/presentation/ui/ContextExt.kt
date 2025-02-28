@file:Keep

package tech.ericwathome.core.presentation.ui

import android.content.Context
import android.widget.Toast
import androidx.annotation.Keep

fun Context.showToast(
    text: String,
    length: Int = Toast.LENGTH_SHORT,
) {
    Toast.makeText(this, text, length).show()
}

fun Context.showToastIfNeeded(
    uiText: UiText?,
    showToast: Boolean,
    length: Int = Toast.LENGTH_SHORT,
) {
    if (showToast) {
        uiText?.asString(this)?.let { showToast(it, length) }
    }
}