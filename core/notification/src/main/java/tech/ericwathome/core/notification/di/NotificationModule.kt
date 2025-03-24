@file:Keep

package tech.ericwathome.core.notification.di

import androidx.annotation.Keep
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import tech.ericwathome.core.notification.NotificationHandler
import tech.ericwathome.core.notification.impl.DefaultNotificationHandler

val notificationModule =
    module {
        singleOf(::DefaultNotificationHandler).bind<NotificationHandler>()
    }