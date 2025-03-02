package tech.ericwathome.currencybuddy

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import tech.ericwathome.auth.presentation.di.authPresentationModule
import tech.ericwathome.converter.data.di.converterDataModule
import tech.ericwathome.converter.presentation.di.converterPresentationModule
import tech.ericwathome.core.data.di.coreDataModule
import tech.ericwathome.core.database.di.databaseModule
import tech.ericwathome.core.network.di.networkModule
import tech.ericwathome.currencybuddy.di.appModule
import tech.ericwathome.currencybuddy.error.CrashHandler
import tech.ericwathome.currencybuddy.error.CrashlyticsTree
import timber.log.Timber

class CurrencyBuddyApp : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()

        setupCrashHandler()

        if (BuildConfig.DEBUG) {
            Timber.plant(
                object : Timber.DebugTree() {
                    override fun createStackElementTag(element: StackTraceElement): String {
                        return super.createStackElementTag(element) + ":" + element.lineNumber
                    }
                },
            )
        } else {
            Timber.plant(CrashlyticsTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@CurrencyBuddyApp)
            workManagerFactory()
            modules(
                appModule,
                coreDataModule,
                databaseModule,
                networkModule,
                converterDataModule,
                authPresentationModule,
                converterPresentationModule,
            )
        }
    }

    private fun setupCrashHandler() {
        Thread.setDefaultUncaughtExceptionHandler(CrashHandler())
    }
}
