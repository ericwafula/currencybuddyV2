package tech.ericwathome.currencybuddy.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import tech.ericwathome.auth.presentation.getstarted.GetStartedScreen
import tech.ericwathome.auth.presentation.sync.SyncScreen

@Composable
fun RootNav(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Routes.Onboarding.OnboardingGraph,
    ) {
        authGraph(navController)
        homeGraph(navController)
    }
}

fun NavGraphBuilder.authGraph(navController: NavController) {
    navigation<Routes.Onboarding.OnboardingGraph>(
        startDestination = Routes.Onboarding.SyncScreen,
    ) {
        composable<Routes.Onboarding.SyncScreen> {
            SyncScreen(
                onNavigateToGetStarted = {
                    navController.navigate(Routes.Onboarding.GetStartedScreen)
                },
                onNavigateToHome = {
                    navController.navigate(Routes.Home.HomeGraph)
                },
                animatedVisibilityScope = this,
            )
        }

        composable<Routes.Onboarding.GetStartedScreen> {
            GetStartedScreen(
                onNavigateToHome = {
                    navController.navigate(Routes.Home.HomeGraph)
                },
                animatedVisibilityScope = this,
            )
        }
    }
}

fun NavGraphBuilder.homeGraph(navController: NavController) {
    navigation<Routes.Home.HomeGraph>(
        startDestination = Routes.Home.HomeScreen,
    ) {
        composable<Routes.Home.HomeScreen> {
            // todo add home screen

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text("Home Screen")
            }
        }

        composable<Routes.Home.Favourites> {
            // todo add favourites screen
        }
    }
}
