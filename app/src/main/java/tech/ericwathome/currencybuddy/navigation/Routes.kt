package tech.ericwathome.currencybuddy.navigation

import kotlinx.serialization.Serializable

object Routes {
    sealed interface Onboarding {
        @Serializable
        data object OnboardingGraph

        @Serializable
        data object GetStartedScreen
    }

    sealed interface Home {
        @Serializable
        data object HomeGraph

        @Serializable
        data object HomeScreen

        @Serializable
        data object Favourites

        @Serializable
        data class CurrencyDetails(val id: Long) : Home
    }
}
