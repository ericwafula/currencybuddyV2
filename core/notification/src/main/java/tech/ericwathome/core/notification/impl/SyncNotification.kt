package tech.ericwathome.core.notification.impl

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import tech.ericwathome.core.domain.ImageUri
import tech.ericwathome.core.domain.notification.strategy.NotificationStrategy

class SyncNotification(
    private val context: Context,
) : NotificationStrategy {
    private val notificationChannel = "Sync"
    private val notificationId = 2
    private val notificationManager = context.getSystemService<NotificationManager>()!!

    init {
        createNotificationChannel()
    }

    override suspend fun show(
        title: String,
        description: String,
        imageUri: ImageUri?,
    ) {
        val notification =
            NotificationCompat.Builder(context, notificationChannel)
                .setSmallIcon(tech.ericwathome.core.presentation.designsystem.R.drawable.ic_bell)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()

        notificationManager.notify(notificationId, notification)
    }

    private fun createNotificationChannel() {
        val channel =
            NotificationChannel(
                notificationChannel,
                notificationChannel.replaceFirstChar { it.uppercase() },
                NotificationManager.IMPORTANCE_DEFAULT,
            )
        notificationManager.createNotificationChannel(channel)
    }
}