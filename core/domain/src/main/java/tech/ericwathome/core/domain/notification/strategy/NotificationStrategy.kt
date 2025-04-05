package tech.ericwathome.core.domain.notification.strategy

interface NotificationStrategy {
    fun show(
        title: String,
        description: String,
    )

    enum class Type {
        SYNC,
        ERROR,
        WARNING,
        INFO,
    }
}