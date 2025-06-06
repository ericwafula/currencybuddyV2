@file:Keep

package tech.ericwathome.core.data.di

import androidx.annotation.Keep
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import tech.ericwathome.core.data.DefaultImageCacheManager
import tech.ericwathome.core.data.converter.OfflineFirstConverterRepository
import tech.ericwathome.core.data.network.HttpClientFactory
import tech.ericwathome.core.data.util.DefaultDispatcherProvider
import tech.ericwathome.core.domain.ImageCacheManager
import tech.ericwathome.core.domain.converter.ConverterRepository
import tech.ericwathome.core.domain.util.DispatcherProvider

val coreDataModule =
    module {
        single {
            HttpClientFactory().build()
        }

        singleOf(::OfflineFirstConverterRepository).bind<ConverterRepository>()

        singleOf(::DefaultDispatcherProvider).bind<DispatcherProvider>()

        singleOf(::DefaultImageCacheManager).bind<ImageCacheManager>()
    }