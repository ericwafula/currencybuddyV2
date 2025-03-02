package tech.ericwathome.currencybuddy

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import tech.ericwathome.core.domain.ConnectionObserver
import tech.ericwathome.core.domain.SessionStorage

class MainViewModel(
    private val sessionStorage: SessionStorage,
    private val connectionObserver: ConnectionObserver,
) : ViewModel() {
    var state by mutableStateOf(MainState())
        private set

    init {
        viewModelScope.launch {
            observeNetworkStatus()

            state = state.copy(isCheckingOnBoardingStatus = true)
            state =
                state.copy(
                    isOnboarded = sessionStorage.isOnboardingComplete(),
                )
            state = state.copy(isCheckingOnBoardingStatus = false)
        }
    }

    private suspend fun observeNetworkStatus() {
        connectionObserver.hasNetworkConnection.collectLatest { isAvailable ->
            state = state.copy(showNetworkPopup = isAvailable.not())
        }
    }

    fun onDismissNetworkError() {
        state = state.copy(showNetworkPopup = false)
    }
}
