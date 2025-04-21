package tech.ericwathome.core.domain.notification.strategy

interface NotificationStrategy {
    fun show(
        title: String,
        description: String,
        icon: Int? = null
    )

    enum class Type {
        SYNC,
        ERROR,
        WARNING,
        INFO,
    }
}