package tech.ericwathome.auth.presentation.getstarted

import androidx.annotation.Keep
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import tech.ericwathome.auth.domain.AuthRepository

@Keep
class GetStartedViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _event = Channel<GetStartedEvent>()
    val event = _event.receiveAsFlow()

    fun onClickGetStarted() {
        viewModelScope.launch {
            authRepository.setIsOnboardingCompleted(true)
            _event.send(GetStartedEvent.OnSuccess)
        }
    }
}