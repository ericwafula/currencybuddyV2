@file:Keep

package tech.ericwathome.core.remote.di

import androidx.annotation.Keep
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import tech.ericwathome.core.domain.ConnectionObserver
import tech.ericwathome.core.domain.LocationObserver
import tech.ericwathome.core.domain.converter.RemoteConverterDataSource
import tech.ericwathome.core.remote.AndroidLocationObserver
import tech.ericwathome.core.remote.DefaultConnectionObserver
import tech.ericwathome.core.remote.converter.KtorRemoteConverterDataSource

val networkModule =
    module {
        singleOf(::KtorRemoteConverterDataSource).bind<RemoteConverterDataSource>()
        singleOf(::DefaultConnectionObserver).bind<ConnectionObserver>()
        single { AndroidLocationObserver(get(), 3000) }.bind<LocationObserver>()
    }