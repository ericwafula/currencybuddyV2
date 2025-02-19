package tech.ericwathome.currencybuddy

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tech.ericwathome.core.domain.SessionStorage

class MainViewModel(
    private val sessionStorage: SessionStorage,
) : ViewModel() {
    var state by mutableStateOf(MainState())
        private set

    init {
        viewModelScope.launch {
            state = state.copy(isCheckingOnBoardingStatus = true)
            state =
                state.copy(
                    isOnboarded = sessionStorage.isOnboardingComplete() == true,
                )
            state = state.copy(isCheckingOnBoardingStatus = false)
        }
    }
}
