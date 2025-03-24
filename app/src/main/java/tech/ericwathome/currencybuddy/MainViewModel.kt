package tech.ericwathome.currencybuddy

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.ericwathome.auth.domain.AuthRepository
import tech.ericwathome.core.domain.ConnectionObserver
import tech.ericwathome.core.domain.SyncEventManager
import tech.ericwathome.core.notification.NotificationHandler

class MainViewModel(
    private val authRepository: AuthRepository,
    private val connectionObserver: ConnectionObserver,
    private val notificationHandler: NotificationHandler,
    private val syncEventManager: SyncEventManager,
) : ViewModel() {
    var state by mutableStateOf(MainState())
        private set
    private val hasGrantedNotificationPermission = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            observeNetworkStatus()

            state = state.copy(isCheckingOnBoardingStatus = true)
            state =
                state.copy(
                    isOnboarded = authRepository.isOnboardingCompletedObservable.firstOrNull() == true,
                )
            state = state.copy(isCheckingOnBoardingStatus = false)
        }

        handleSyncEvent()
    }

    fun submitNotificationPermissionInfo(
        hasGrantedNotificationPermission: Boolean,
        showNotificationPermissionRationale: Boolean,
    ) {
        state =
            state.copy(
                showNotificationPermissionRationale = showNotificationPermissionRationale,
            )
        this.hasGrantedNotificationPermission.update { hasGrantedNotificationPermission }
    }

    private suspend fun observeNetworkStatus() {
        connectionObserver.hasNetworkConnection.collectLatest { isAvailable ->
            state = state.copy(showNetworkPopup = isAvailable.not())
        }
    }

    private fun handleSyncEvent() {
        syncEventManager
            .event
            .combine(hasGrantedNotificationPermission) { event, hasGrantedNotificationPermission ->
                when (event) {
                    SyncEventManager.SyncEvent.SyncMetadataSuccess -> {
                        showSyncNotification(
                            title = "Sync Complete",
                            message = "Currency sync completed successfully",
                            hasGrantedNotificationPermission = hasGrantedNotificationPermission,
                        )
                    }

                    SyncEventManager.SyncEvent.SyncMetadataError -> {
                        showSyncNotification(
                            title = "Sync Failure",
                            message = "Currency sync failed",
                            hasGrantedNotificationPermission = hasGrantedNotificationPermission,
                        )
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun showSyncNotification(
        title: String,
        message: String,
        hasGrantedNotificationPermission: Boolean,
    ) {
        if (hasGrantedNotificationPermission) {
            notificationHandler.showSimpleNotification(
                title = title,
                message = message,
                notificationType = NotificationHandler.NotificationType.Sync,
            )
        }
    }
}
