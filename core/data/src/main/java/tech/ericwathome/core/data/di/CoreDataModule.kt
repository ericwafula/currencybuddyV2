package tech.ericwathome.core.data.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import tech.ericwathome.core.data.EncryptedSessionStorage
import tech.ericwathome.core.data.network.HttpClientFactory
import tech.ericwathome.core.domain.SessionStorage

val coreDataModule = module {
    single {
        HttpClientFactory().build()
    }
    singleOf(::EncryptedSessionStorage).bind<SessionStorage>()
}