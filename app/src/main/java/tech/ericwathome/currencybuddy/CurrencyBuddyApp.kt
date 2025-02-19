package tech.ericwathome.currencybuddy

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import tech.ericwathome.core.data.di.coreDataModule
import tech.ericwathome.core.database.di.databaseModule
import tech.ericwathome.core.network.converter.di.networkModule
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
            modules(
                appModule,
                coreDataModule,
                databaseModule,
                networkModule,
            )
        }
    }

    private fun setupCrashHandler() {
        Thread.setDefaultUncaughtExceptionHandler(CrashHandler())
    }
}
