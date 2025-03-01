@file:Keep

package tech.ericwathome.converter.presentation.di

import androidx.annotation.Keep
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import tech.ericwathome.converter.presentation.ConverterViewModel

val converterPresentationModule =
    module {
        viewModelOf(::ConverterViewModel)
    }