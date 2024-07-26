package dev.mjanusz.recruitmentapp.ui

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.mjanusz.recruitmentapp.AppState
import dev.mjanusz.recruitmentapp.rememberAppState
import dev.mjanusz.recruitmentapp.ui.feature.repos.RepositoryDetailsScreen
import dev.mjanusz.recruitmentapp.ui.feature.trending.TrendingScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Home(
    appState: AppState = rememberAppState()
) {
    NavHost(
        navController = appState.navController,
        startDestination = "trending",
    ) {
        composable("trending") {
            TrendingScreen(
                appState = appState,
            )
        }
        composable("repository/{owner}/{repo}") { navBackStackEntry ->
            RepositoryDetailsScreen(
                owner = navBackStackEntry.arguments?.getString("owner")!!,
                repo = navBackStackEntry.arguments?.getString("repo")!!
            )
        }
    }
}