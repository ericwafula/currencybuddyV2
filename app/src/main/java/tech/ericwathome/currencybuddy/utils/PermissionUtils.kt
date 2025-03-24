package tech.ericwathome.currencybuddy.utils

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.activity.ComponentActivity
import tech.ericwathome.core.presentation.ui.hasGrantedPermission

fun ComponentActivity.shouldShowNotificationPermissionRationale(): Boolean {
    return Build.VERSION.SDK_INT >= 33 &&
        shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)
}

fun Context.hasNotificationPermission(): Boolean {
    return if (Build.VERSION.SDK_INT >= 33) {
        hasGrantedPermission(Manifest.permission.POST_NOTIFICATIONS)
    } else {
        true
    }
}