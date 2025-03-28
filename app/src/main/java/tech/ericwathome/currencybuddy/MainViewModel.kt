package tech.ericwathome.currencybuddy

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
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
            connectionObserver.hasNetworkConnection
                .collectLatest { isAvailable ->
                    if (isAvailable) {
                        state = state.copy(showNetworkPopup = false)
                    } else {
                        delay(500) // Only show popup if disconnected for at least 500ms
                        // Re-check in case network came back during delay
                        if (!connectionObserver.hasNetworkConnection.value) {
                            state = state.copy(showNetworkPopup = true)
                        }
                    }
                }
        }
    }
}
