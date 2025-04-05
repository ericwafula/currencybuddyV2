package tech.ericwathome.core.domain.notification

import tech.ericwathome.core.domain.notification.strategy.NotificationStrategy

interface NotificationFactory {
    fun get(type: NotificationStrategy.Type): NotificationStrategy
}