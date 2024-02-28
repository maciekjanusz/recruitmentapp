package dev.mjanusz.recruitmentapp.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.mjanusz.recruitmentapp.AppState
import dev.mjanusz.recruitmentapp.R
import dev.mjanusz.recruitmentapp.rememberAppState
import dev.mjanusz.recruitmentapp.ui.theme.AppTypography
import dev.mjanusz.recruitmentapp.ui.users.details.UserScreen
import dev.mjanusz.recruitmentapp.ui.users.list.UserListScreen
import dev.mjanusz.recruitmentapp.ui.users.search.UserSearchScreen

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Home(
    modifier: Modifier = Modifier,
    appState: AppState = rememberAppState()
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.home_display_title), style =
                        AppTypography.titleLarge
                    )
                },
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = appState.navController,
            startDestination = "users",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("users") {
                Column {
                    UserSearchScreen(
                        appState = appState,
                        modifier = modifier
                            .safeContentPadding()
                            .align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider()
                    UserListScreen(
                        appState = appState,
                    )
                }
            }
            composable("users/{username}") { navBackStackEntry ->
                UserScreen(
                    username = navBackStackEntry.arguments?.getString("username") ?: "",
                )
            }
        }
    }
}