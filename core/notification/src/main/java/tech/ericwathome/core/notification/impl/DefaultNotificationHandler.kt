package tech.ericwathome.core.notification.impl

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import tech.ericwathome.core.notification.NotificationHandler
import tech.ericwathome.core.presentation.ui.UiText

internal class DefaultNotificationHandler(
    private val context: Context,
) : NotificationHandler {
    private val notificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private val baseNotification by lazy {
        NotificationCompat.Builder(context, NotificationHandler.NotificationType.General.channelId)
            .setSmallIcon(tech.ericwathome.core.presentation.designsystem.R.drawable.ic_bell)
    }

    init {
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        val channels =
            NotificationHandler.NotificationType.all.map { channel ->
                NotificationChannel(
                    channel.channelId,
                    channel.channelId.replaceFirstChar { it.uppercase() },
                    NotificationManager.IMPORTANCE_DEFAULT,
                )
            }

        notificationManager.createNotificationChannels(channels)
    }

    override fun showSimpleNotification(
        title: UiText,
        message: UiText,
        notificationType: NotificationHandler.NotificationType,
    ) {
        val notification =
            baseNotification
                .setContentTitle(title.asString(context))
                .setContentText(message.asString(context))
                .setChannelId(notificationType.channelId)
                .build()

        notificationManager.notify(notificationType.notificationId, notification)
    }

    override fun showSimpleUniqueNotification(
        title: UiText,
        message: UiText,
        channels: NotificationHandler.NotificationType,
    ) {
        val notification =
            baseNotification
                .setContentTitle(title.asString(context))
                .setContentText(message.asString(context))
                .setChannelId(channels.channelId)
                .build()

        val uniqueNotificationId = (System.currentTimeMillis() % Int.MAX_VALUE).toInt()

        notificationManager.notify(uniqueNotificationId, notification)
    }
}