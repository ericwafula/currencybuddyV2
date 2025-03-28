package tech.ericwathome.currencybuddy

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import tech.ericwathome.auth.domain.AuthRepository
import tech.ericwathome.core.domain.ConnectionObserver

class MainViewModel(
    private val authRepository: AuthRepository,
    private val connectionObserver: ConnectionObserver,
) : ViewModel() {
    var state by mutableStateOf(MainState())
        private set

    init {
        observeNetworkStatus()
        checkOnboardingStatus()
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
}
