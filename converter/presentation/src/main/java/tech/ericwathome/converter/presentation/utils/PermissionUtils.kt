package tech.ericwathome.converter.presentation.utils

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.activity.ComponentActivity
import tech.ericwathome.core.presentation.ui.hasGrantedPermission

fun ComponentActivity.shouldShowNotificationPermissionRationale(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
        shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)
}

fun ComponentActivity.shouldShowLocationPermissionRationale(): Boolean {
    return shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
}

fun Context.hasNotificationPermission(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        hasGrantedPermission(Manifest.permission.POST_NOTIFICATIONS)
    } else {
        true
    }
}

fun Context.hasLocationPermissions(): Boolean {
    return hasGrantedPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
        hasGrantedPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
}