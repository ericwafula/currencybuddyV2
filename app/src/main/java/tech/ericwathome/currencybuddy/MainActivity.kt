package tech.ericwathome.currencybuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.compose.KoinContext
import tech.ericwathome.core.presentation.designsystem.CurrencybuddyTheme
import tech.ericwathome.core.presentation.designsystem.components.CurrencyBuddyNetworkLayout
import tech.ericwathome.core.presentation.ui.CollectOneTimeEvent
import tech.ericwathome.core.presentation.ui.rememberOpenNetworkSettings
import tech.ericwathome.currencybuddy.navigation.RootNav
import tech.ericwathome.widget.presentation.updateCurrencyWidget

class MainActivity : ComponentActivity() {
    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.state.isCheckingOnBoardingStatus
            }
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            KoinContext {
                val openNetworkSettings = rememberOpenNetworkSettings()

                CollectOneTimeEvent(viewModel.event) { event ->
                    when (event) {
                        is MainEvent.UpdateCurrencyWidget -> updateCurrencyWidget(applicationContext, event.exchangeRate)
                    }
                }

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
