package tech.ericwathome.core.notification

import tech.ericwathome.core.presentation.ui.UiText

interface NotificationHandler {
    fun showSimpleNotification(
        title: UiText,
        message: UiText,
        notificationType: NotificationType,
    )

    fun showSimpleUniqueNotification(
        title: UiText,
        message: UiText,
        channels: NotificationType,
    )

    sealed class NotificationType(val channelId: String, val notificationId: Int) {
        data object General : NotificationType("general", 1)

        data object Sync : NotificationType("sync", 2)

        companion object {
            val all: List<NotificationType> = listOf(General, Sync)
        }
    }
}