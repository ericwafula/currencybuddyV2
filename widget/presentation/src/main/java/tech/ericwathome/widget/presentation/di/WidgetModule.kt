package tech.ericwathome.widget.presentation.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import tech.ericwathome.core.domain.widget.ConverterWidgetUpdater
import tech.ericwathome.core.domain.widget.WidgetUpdater
import tech.ericwathome.widget.presentation.DefaultConverterWidgetUpdater
import tech.ericwathome.widget.presentation.DefaultWidgetUpdater

val widgetModule =
    module {
        singleOf(::DefaultConverterWidgetUpdater).bind<ConverterWidgetUpdater>()
        singleOf(::DefaultWidgetUpdater).bind<WidgetUpdater>()
    }