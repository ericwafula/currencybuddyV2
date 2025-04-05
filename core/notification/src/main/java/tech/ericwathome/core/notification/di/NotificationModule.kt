@file:Keep

package tech.ericwathome.core.notification.di

import androidx.annotation.Keep
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import tech.ericwathome.core.domain.notification.NotificationFactory
import tech.ericwathome.core.domain.notification.strategy.NotificationStrategy
import tech.ericwathome.core.notification.impl.SyncNotification

val notificationModule =
    module {
        single(named(NotificationStrategy.Type.SYNC)) { SyncNotification(get()) } bind NotificationStrategy::class
        single<NotificationFactory> {
            object : NotificationFactory {
                override fun get(type: NotificationStrategy.Type): NotificationStrategy {
                    return when (type) {
                        NotificationStrategy.Type.SYNC -> get(named(NotificationStrategy.Type.SYNC))
                        else -> throw IllegalArgumentException("Unknown notification type: $type")
                    }
                }
            }
        }
    }