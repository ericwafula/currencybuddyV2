package tech.ericwathome.core.notification

import androidx.annotation.Keep

@Keep
interface NotificationHandler {
    fun showSimpleNotification(
        title: String,
        message: String,
        notificationType: NotificationType,
    )

    fun showSimpleUniqueNotification(
        title: String,
        message: String,
        channels: NotificationType,
    )

    @Keep
    sealed class NotificationType(val channelId: String, val notificationId: Int) {
        @Keep
        data object General : NotificationType("general", 1)

        @Keep
        data object Sync : NotificationType("sync", 2)

        companion object {
            val all: List<NotificationType> = listOf(General, Sync)
        }
    }
}