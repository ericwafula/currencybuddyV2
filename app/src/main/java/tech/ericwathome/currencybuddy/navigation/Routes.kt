package tech.ericwathome.currencybuddy.navigation

import kotlinx.serialization.Serializable

object Routes {
    sealed interface Onboarding {
        @Serializable
        data object OnboardingGraph

        @Serializable
        data object GetStartedScreen

        @Serializable
        data object SyncScreen
    }

    sealed interface Home {
        @Serializable
        data object HomeGraph

        @Serializable
        data object ConverterScreen

        @Serializable
        data object Favourites
    }
}
