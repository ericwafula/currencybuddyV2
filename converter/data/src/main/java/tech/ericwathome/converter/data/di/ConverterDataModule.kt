@file:Keep

package tech.ericwathome.converter.data.di

import androidx.annotation.Keep
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import tech.ericwathome.converter.data.ConverterWorkerScheduler
import tech.ericwathome.converter.data.DefaultSyncEventManager
import tech.ericwathome.converter.data.workers.SyncCurrencyMetaDataWorker
import tech.ericwathome.converter.data.workers.SyncSelectedCurrencyPairWorker
import tech.ericwathome.core.domain.ConverterScheduler
import tech.ericwathome.core.domain.SyncEventManager

val converterDataModule =
    module {
        workerOf(::SyncCurrencyMetaDataWorker)
        workerOf(::SyncSelectedCurrencyPairWorker)
        singleOf(::ConverterWorkerScheduler).bind<ConverterScheduler>()
        singleOf(::DefaultSyncEventManager).bind<SyncEventManager>()
    }