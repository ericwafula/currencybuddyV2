@file:Keep

package tech.ericwathome.auth.data.di

import androidx.annotation.Keep
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import tech.ericwathome.auth.data.DefaultAuthRepository
import tech.ericwathome.auth.domain.AuthRepository

val authDataModule = module {
    singleOf(::DefaultAuthRepository).bind<AuthRepository>()
}