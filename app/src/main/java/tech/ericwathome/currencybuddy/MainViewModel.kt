package tech.ericwathome.currencybuddy

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import tech.ericwathome.auth.domain.AuthRepository
import tech.ericwathome.core.domain.ConnectionObserver
import tech.ericwathome.core.domain.converter.ConverterRepository
import tech.ericwathome.core.domain.util.DispatcherProvider
import tech.ericwathome.core.domain.widget.WidgetUpdater

class MainViewModel(
    private val authRepository: AuthRepository,
    private val connectionObserver: ConnectionObserver,
    private val converterRepository: ConverterRepository,
    private val widgetUpdater: WidgetUpdater,
    private val dispatchers: DispatcherProvider,
) : ViewModel() {
    var state by mutableStateOf(MainState())
        private set

    init {
        observeNetworkStatus()
        checkOnboardingStatus()
        observeExchangeRateUpdateEvents()
    }

    private fun checkOnboardingStatus() {
        viewModelScope.launch {
            state = state.copy(isCheckingOnBoardingStatus = true)
            state =
                state.copy(
                    isOnboarded = authRepository.isOnboardingCompletedObservable.firstOrNull() == true,
                )
            state = state.copy(isCheckingOnBoardingStatus = false)
        }
    }

    private fun observeNetworkStatus() {
        viewModelScope.launch {
            connectionObserver
                .networkStatus
                .collectLatest { networkStatus ->
                    state =
                        when (networkStatus) {
                            ConnectionObserver.NetworkStatus.Available -> {
                                state.copy(showNetworkPopup = false)
                            }

                            ConnectionObserver.NetworkStatus.Checking -> {
                                state.copy(showNetworkPopup = false)
                            }

                            ConnectionObserver.NetworkStatus.Unavailable -> {
                                state.copy(showNetworkPopup = true)
                            }
                        }
                }
        }
    }

    private fun observeExchangeRateUpdateEvents() {
        viewModelScope.launch(dispatchers.io) {
            converterRepository
                .exchangeRateObservable
                .filterNotNull()
                .collectLatest {
                    widgetUpdater.updateConverterWidget(it)
                }
        }
    }
}
