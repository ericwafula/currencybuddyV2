package tech.ericwathome.currencybuddy.di

import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import tech.ericwathome.currencybuddy.CurrencyBuddyApp
import tech.ericwathome.currencybuddy.MainViewModel

val appModule =
    module {
        single<CoroutineScope> {
            (androidApplication() as CurrencyBuddyApp).applicationScope
        }

        viewModelOf(::MainViewModel)
    }
