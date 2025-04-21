package tech.ericwathome.core.domain.notification.strategy

import tech.ericwathome.core.domain.ImageUri

interface NotificationStrategy {
    suspend fun show(
        title: String,
        description: String,
        imageUri: ImageUri? = null,
    )

    enum class Type {
        SYNC,
        ERROR,
        WARNING,
        INFO,
    }
}