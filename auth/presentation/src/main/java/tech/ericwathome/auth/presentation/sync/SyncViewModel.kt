@file:Keep

package tech.ericwathome.auth.presentation.sync

import androidx.annotation.Keep
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.ericwathome.core.domain.SessionStorage
import tech.ericwathome.core.domain.converter.ConverterRepository
import tech.ericwathome.core.domain.util.Result
import tech.ericwathome.core.presentation.ui.asUiText

class SyncViewModel(
    private val converterRepository: ConverterRepository,
    private val sessionStorage: SessionStorage,
) : ViewModel() {
    private val _event = Channel<SyncEvent>()
    val event = _event.receiveAsFlow()

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing =
        _isSyncing
            .onStart { syncMetadata() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = false,
            )

    private fun syncMetadata() {
        _isSyncing.update { true }
        viewModelScope.launch {
            val result = converterRepository.syncCurrencyMetadata()
            _isSyncing.update { false }
            val hasFinishedOnboarding = sessionStorage.isOnboardingComplete()

            if (hasFinishedOnboarding) {
                when (result) {
                    is Result.Error -> _event.send(SyncEvent.OnNavigateToHome(result.error.asUiText(), showToast = true))
                    is Result.Success -> _event.send(SyncEvent.OnNavigateToHome(showToast = false))
                }
                return@launch
            }

            when (result) {
                is Result.Error -> _event.send(SyncEvent.OnNavigateToGetStarted(result.error.asUiText(), showToast = true))
                is Result.Success -> _event.send(SyncEvent.OnNavigateToGetStarted(showToast = false))
            }
        }
    }
}