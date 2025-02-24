package tech.ericwathome.currencybuddy.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController

@Composable
fun RootNav(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Routes.Onboarding.OnboardingGraph,
    ) {
        onboardingGraph(navController)
        homeGraph(navController)
    }
}

fun NavGraphBuilder.onboardingGraph(navController: NavController) {
    navigation<Routes.Onboarding.OnboardingGraph>(
        startDestination = Routes.Onboarding.GetStartedScreen,
    ) {
        composable<Routes.Onboarding.GetStartedScreen> {
            val lifecycleOwner = LocalLifecycleOwner.current

            // todo add get started screen

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text("Get Started Screen")
            }
        }
    }
}

fun NavGraphBuilder.homeGraph(navController: NavController) {
    navigation<Routes.Home.HomeGraph>(
        startDestination = Routes.Home.HomeScreen,
    ) {
        composable<Routes.Home.HomeScreen> {
            // todo add home screen
        }

        composable<Routes.Home.Favourites> {
            // todo add favourites screen
        }
    }
}
