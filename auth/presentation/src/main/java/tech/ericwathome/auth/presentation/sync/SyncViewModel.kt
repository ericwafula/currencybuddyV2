package tech.ericwathome.auth.presentation.sync

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
import tech.ericwathome.core.domain.converter.ConverterRepository
import tech.ericwathome.core.domain.util.Result
import tech.ericwathome.core.presentation.ui.asUiText

class SyncViewModel(
    private val converterRepository: ConverterRepository,
) : ViewModel() {
    private val _event = Channel<SyncEvent>()
    val event = _event.receiveAsFlow()

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing =
        _isSyncing
            .onStart { syncData() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = false,
            )

    private fun syncData() {
        syncExchangeRates()
        syncMetadata()
    }

    private fun syncExchangeRates() {
        _isSyncing.update { true }
        viewModelScope.launch {
            when (val result = converterRepository.syncSavedExchangeRates()) {
                is Result.Error -> {
                    _isSyncing.update { false }
                    _event.send(SyncEvent.OnError(result.error.asUiText()))
                }
                is Result.Success -> {
                    _isSyncing.update { false }
                    _event.send(SyncEvent.OnSuccess)
                }
            }
        }
    }

    private fun syncMetadata() {
        _isSyncing.update { true }
        viewModelScope.launch {
            when (val result = converterRepository.syncCurrencyMetadata()) {
                is Result.Error -> {
                    _isSyncing.update { false }
                    _event.send(SyncEvent.OnError(result.error.asUiText()))
                }
                is Result.Success -> {
                    _isSyncing.update { false }
                    _event.send(SyncEvent.OnSuccess)
                }
            }
        }
    }
}