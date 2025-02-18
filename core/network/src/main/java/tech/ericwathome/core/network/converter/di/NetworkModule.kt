package tech.ericwathome.core.network.converter.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import tech.ericwathome.core.domain.converter.RemoteConverterDataSource
import tech.ericwathome.core.network.converter.KtorRemoteConverterDataSource

val networkModule = module {
    singleOf(::KtorRemoteConverterDataSource).bind<RemoteConverterDataSource>()
}