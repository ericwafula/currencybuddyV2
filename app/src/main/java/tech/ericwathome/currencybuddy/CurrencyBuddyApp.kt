package tech.ericwathome.currencybuddy

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import tech.ericwathome.auth.data.di.authDataModule
import tech.ericwathome.auth.presentation.di.authPresentationModule
import tech.ericwathome.converter.data.di.converterDataModule
import tech.ericwathome.converter.presentation.di.converterPresentationModule
import tech.ericwathome.core.data.di.coreDataModule
import tech.ericwathome.core.local.di.localModule
import tech.ericwathome.core.notification.di.notificationModule
import tech.ericwathome.core.remote.di.networkModule
import tech.ericwathome.currencybuddy.di.appModule
import tech.ericwathome.currencybuddy.error.CrashHandler
import tech.ericwathome.currencybuddy.error.CrashlyticsTree
import tech.ericwathome.widget.presentation.di.widgetModule
import timber.log.Timber

class CurrencyBuddyApp : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()

        initTimber()
        setupCrashHandler()
        initKoin()
    }

    private fun initTimber() {
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
    }

    private fun setupCrashHandler() {
        Thread.setDefaultUncaughtExceptionHandler(CrashHandler())
    }

    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@CurrencyBuddyApp)
            workManagerFactory()
            modules(
                appModule,
                coreDataModule,
                localModule,
                networkModule,
                converterDataModule,
                authPresentationModule,
                converterPresentationModule,
                authDataModule,
                notificationModule,
                widgetModule,
            )
        }
    }
}
