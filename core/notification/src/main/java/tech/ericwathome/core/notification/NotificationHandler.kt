package tech.ericwathome.core.notification

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

    sealed class NotificationType(val channelId: String, val notificationId: Int) {
        data object General : NotificationType("general", 1)

        data object Sync : NotificationType("sync", 2)

        companion object {
            val all: List<NotificationType> = listOf(General, Sync)
        }
    }
}