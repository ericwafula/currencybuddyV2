package tech.ericwathome.currencybuddy.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import tech.ericwathome.auth.presentation.getstarted.GetStartedScreen
import tech.ericwathome.auth.presentation.sync.SyncScreen
import tech.ericwathome.converter.presentation.ConverterScreen

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
                    navController.navigate(Routes.Onboarding.GetStartedScreen) {
                        popUpTo(Routes.Onboarding.SyncScreen) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Routes.Home.HomeGraph) {
                        popUpTo(Routes.Onboarding.OnboardingGraph) {
                            inclusive = true
                        }
                    }
                },
                animatedVisibilityScope = this,
            )
        }

        composable<Routes.Onboarding.GetStartedScreen> {
            GetStartedScreen(
                onNavigateToHome = {
                    navController.navigate(Routes.Home.HomeGraph) {
                        popUpTo(Routes.Onboarding.OnboardingGraph) {
                            inclusive = true
                        }
                    }
                },
                animatedVisibilityScope = this,
            )
        }
    }
}

fun NavGraphBuilder.homeGraph(navController: NavController) {
    navigation<Routes.Home.HomeGraph>(
        startDestination = Routes.Home.ConverterScreen,
    ) {
        composable<Routes.Home.ConverterScreen> {
            ConverterScreen()
        }

        composable<Routes.Home.Favourites> {
            // todo add favourites screen
        }
    }
}
