@file:Keep

package tech.ericwathome.core.presentation.ui

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Keep
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import timber.log.Timber

@Composable
fun rememberOpenNetworkSettings(): () -> Unit {
    val context = LocalContext.current
    val networkSettingsLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult(),
        ) { result ->
            Timber.d("Network settings result: ${result.resultCode}")
        }

    val settingsIntent =
        remember {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY)
            } else {
                Intent(Settings.ACTION_WIRELESS_SETTINGS)
            }
        }

    return {
        if (context is Activity) {
            networkSettingsLauncher.launch(settingsIntent)
        } else {
            // If the context isn't an Activity, launch the intent directly.
            context.startActivity(settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }
    }
}