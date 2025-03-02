package tech.ericwathome.currencybuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.ericwathome.core.presentation.designsystem.CurrencybuddyTheme
import tech.ericwathome.core.presentation.designsystem.components.NetworkLayout
import tech.ericwathome.currencybuddy.navigation.RootNav

class MainActivity : ComponentActivity() {
    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.state.isCheckingOnBoardingStatus
            }
        }
        enableEdgeToEdge()

        setContent {
            CurrencybuddyTheme {
                NetworkLayout(
                    onDismiss = { viewModel.onDismissNetworkError() },
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
