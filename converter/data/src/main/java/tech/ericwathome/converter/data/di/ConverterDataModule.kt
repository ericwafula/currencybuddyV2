@file:Keep

package tech.ericwathome.converter.data.di

import androidx.annotation.Keep
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import tech.ericwathome.converter.data.ConverterWorkerScheduler
import tech.ericwathome.converter.data.workers.SyncCurrencyMetaDataWorker
import tech.ericwathome.converter.data.workers.SyncExchangeRatesWorker
import tech.ericwathome.core.domain.ConverterScheduler

val converterDataModule =
    module {
        workerOf(::SyncCurrencyMetaDataWorker)
        workerOf(::SyncExchangeRatesWorker)
        singleOf(::ConverterWorkerScheduler).bind<ConverterScheduler>()
    }