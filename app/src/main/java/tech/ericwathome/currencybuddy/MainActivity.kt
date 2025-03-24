package tech.ericwathome.currencybuddy

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.compose.KoinContext
import tech.ericwathome.core.presentation.designsystem.CurrencybuddyTheme
import tech.ericwathome.core.presentation.designsystem.components.CurrencyBuddyNetworkLayout
import tech.ericwathome.core.presentation.ui.rememberOpenNetworkSettings
import tech.ericwathome.currencybuddy.navigation.RootNav
import tech.ericwathome.currencybuddy.utils.shouldShowNotificationPermissionRationale

class MainActivity : ComponentActivity() {
    private val viewModel by viewModel<MainViewModel>()
    private val permissionLauncher =
        registerForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
        ) { isGranted ->
            val showNotificationRationale = shouldShowNotificationPermissionRationale()

            viewModel.submitNotificationPermissionInfo(
                hasGrantedNotificationPermission = isGranted,
                showNotificationPermissionRationale = showNotificationRationale,
            )
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.state.isCheckingOnBoardingStatus
            }
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            viewModel.submitNotificationPermissionInfo(
                hasGrantedNotificationPermission = true,
                showNotificationPermissionRationale = false,
            )
        }

        setContent {
            KoinContext {
                val openNetworkSettings = rememberOpenNetworkSettings()

                CurrencybuddyTheme {
                    CurrencyBuddyNetworkLayout(
                        onClickConnect = openNetworkSettings,
                        showNetworkPopup = viewModel.state.showNetworkPopup,
                    ) {
                        if (!viewModel.state.isCheckingOnBoardingStatus) {
                            val navController = rememberNavController()
                            RootNav(
                                modifier = Modifier,
                                navController = navController,
                            )
                        }
                    }
                }
            }
        }
    }
}
