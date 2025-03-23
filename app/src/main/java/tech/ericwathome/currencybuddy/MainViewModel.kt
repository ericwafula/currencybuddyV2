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

    init {
        viewModelScope.launch {
            observeNetworkStatus()
            handleSyncEvent()

            state = state.copy(isCheckingOnBoardingStatus = true)
            state =
                state.copy(
                    isOnboarded = authRepository.isOnboardingCompletedObservable.firstOrNull() == true,
                )
            state = state.copy(isCheckingOnBoardingStatus = false)
        }
    }

    fun submitNotificationPermissionInfo(
        hasGrantedNotificationPermission: Boolean,
        showNotificationPermissionRationale: Boolean,
    ) {
        state =
            state.copy(
                hasGrantedNotificationPermission = hasGrantedNotificationPermission,
                showNotificationPermissionRationale = showNotificationPermissionRationale,
            )
    }

    private suspend fun observeNetworkStatus() {
        connectionObserver.hasNetworkConnection.collectLatest { isAvailable ->
            state = state.copy(showNetworkPopup = isAvailable.not())
        }
    }

    private suspend fun handleSyncEvent() {
        syncEventManager.event.collectLatest { event ->
            when (event) {
                SyncEventManager.SyncEvent.SyncMetadataSuccess -> {
                    showSyncNotification(
                        title = "Sync Complete",
                        message = "Currency sync completed successfully",
                    )
                }
                SyncEventManager.SyncEvent.SyncMetadataError -> {
                    showSyncNotification(
                        title = "Sync Failure",
                        message = "Currency sync failed",
                    )
                }
            }
        }
    }

    private fun showSyncNotification(
        title: String,
        message: String,
    ) {
        notificationHandler.showSimpleNotification(
            title = title,
            message = message,
            notificationType = NotificationHandler.NotificationType.Sync,
        )
    }
}
