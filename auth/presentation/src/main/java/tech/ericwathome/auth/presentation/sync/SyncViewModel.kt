package tech.ericwathome.auth.presentation.sync

import androidx.annotation.Keep
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.ericwathome.auth.domain.AuthRepository
import tech.ericwathome.core.domain.converter.ConverterRepository
import tech.ericwathome.core.domain.converter.ConverterScheduler
import tech.ericwathome.core.domain.util.Result
import kotlin.time.Duration.Companion.minutes

@Keep
class SyncViewModel(
    private val converterRepository: ConverterRepository,
    private val authRepository: AuthRepository,
    private val converterScheduler: ConverterScheduler,
) : ViewModel() {
    private val _event = Channel<SyncEvent>()
    val event = _event.receiveAsFlow()

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing =
        _isSyncing
            .onStart { handleSyncing() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = false,
            )

    private fun handleSyncing() {
        _isSyncing.update { true }

        viewModelScope.launch {
            val lastSyncTimestamp = converterRepository.lastMetadataSyncTimestampObservable.firstOrNull() ?: 0
            val elapsedTime = System.currentTimeMillis() - lastSyncTimestamp
            val hasFinishedOnboarding = authRepository.isOnboardingCompletedObservable.firstOrNull() == true

            if (elapsedTime >= 30.minutes.inWholeMilliseconds) {
                syncMetadata(hasFinishedOnboarding)
                return@launch
            }

            navigateAfterSync(hasFinishedOnboarding)
        }
    }

    private suspend fun syncMetadata(hasFinishedOnboarding: Boolean) {
        val result = converterRepository.syncCurrencyMetadata()
        _isSyncing.update { false }

        if (result is Result.Error) {
            startSyncWork()
        }

        navigateAfterSync(hasFinishedOnboarding)
    }

    private suspend fun navigateAfterSync(hasFinishedOnboarding: Boolean) {
        val event =
            if (hasFinishedOnboarding) {
                SyncEvent.OnNavigateToHome(showToast = false)
            } else {
                SyncEvent.OnNavigateToGetStarted(showToast = false)
            }
        _event.send(event)
    }

    private suspend fun startSyncWork() {
        converterScheduler.scheduleSync(ConverterScheduler.SyncType.FetchCurrencyMetadata)
    }
}