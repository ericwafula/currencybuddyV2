package tech.ericwathome.core.data.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import tech.ericwathome.core.data.EncryptedSessionStorage
import tech.ericwathome.core.data.converter.DefaultConverterRepository
import tech.ericwathome.core.data.network.HttpClientFactory
import tech.ericwathome.core.domain.SessionStorage
import tech.ericwathome.core.domain.converter.ConverterRepository

val coreDataModule =
    module {
        single {
            HttpClientFactory().build()
        }
        singleOf(::EncryptedSessionStorage).bind<SessionStorage>()

        singleOf(::DefaultConverterRepository).bind<ConverterRepository>()
    }