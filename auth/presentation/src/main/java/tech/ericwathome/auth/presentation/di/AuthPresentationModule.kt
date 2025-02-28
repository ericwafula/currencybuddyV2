@file:Keep

package tech.ericwathome.auth.presentation.di

import androidx.annotation.Keep
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import tech.ericwathome.auth.presentation.getstarted.GetStartedViewModel
import tech.ericwathome.auth.presentation.sync.SyncViewModel

val authPresentationModule =
    module {
        viewModelOf(::SyncViewModel)
        viewModelOf(::GetStartedViewModel)
    }