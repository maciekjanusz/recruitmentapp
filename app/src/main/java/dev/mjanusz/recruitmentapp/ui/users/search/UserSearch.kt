package dev.mjanusz.recruitmentapp.ui.users.search

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import dev.mjanusz.recruitmentapp.AppState
import dev.mjanusz.recruitmentapp.R
import dev.mjanusz.recruitmentapp.ui.model.User
import dev.mjanusz.recruitmentapp.ui.theme.AppTypography
import dev.mjanusz.recruitmentapp.ui.users.list.UserListContent
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun UserSearchScreen(
    appState: AppState,
    modifier: Modifier = Modifier,
    userSearchViewModel: UserSearchViewModel = hiltViewModel()
) {
    // state
    val query by userSearchViewModel.searchQuery.collectAsStateWithLifecycle()
    val active by userSearchViewModel.searchActive.collectAsStateWithLifecycle()
    // items
    val users = userSearchViewModel.searchResults.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        userSearchViewModel.userClicked.collect {
            appState.navController.navigate("users/${it.login}") {
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    UserSearchContent(
        searchQuery = query,
        active = active,
        items = users,
        onQueryChange = userSearchViewModel::onQueryChange,
        onActiveChange = userSearchViewModel::onActiveChange,
        onItemClicked = userSearchViewModel::onUserClicked,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSearchContent(
    searchQuery: String,
    onQueryChange: (query: String) -> Unit,
    active: Boolean,
    onActiveChange: (active: Boolean) -> Unit,
    items: LazyPagingItems<User>,
    modifier: Modifier = Modifier,
    onItemClicked: (user: User) -> Unit = {},
) {
    SearchBar(
        query = searchQuery,
        onQueryChange = onQueryChange,
        onSearch = onQueryChange,
        active = active,
        onActiveChange = onActiveChange,
        placeholder = {
            Text(
                text = stringResource(id = R.string.search_placeholder), style =
                AppTypography.bodyLarge
            )
        },
        leadingIcon = {
            Icon(
                Icons.Default.Menu,
                contentDescription = stringResource(id = R.string.cd_menu)
            )
        },
        trailingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = stringResource(id = R.string.cd_search)
            )
        },
        colors = SearchBarDefaults.colors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            dividerColor = MaterialTheme.colorScheme.onSecondaryContainer,
            inputFieldColors = TextFieldDefaults.colors()
        ),
        modifier = modifier
    ) {
        UserListContent(
            users = items,
            clickListener = onItemClicked,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview
@Composable
fun PreviewInactiveUserSearchContent() {
    UserSearchContent(
        searchQuery = "",
        onQueryChange = {},
        active = false,
        onActiveChange = {},
        items = emptyFlow<PagingData<User>>().collectAsLazyPagingItems(),
        onItemClicked = {}
    )
}

@Preview
@Composable
fun PreviewActiveUserSearchContent() {
    UserSearchContent(
        searchQuery = "",
        onQueryChange = {},
        active = true,
        onActiveChange = {},
        items = emptyFlow<PagingData<User>>().collectAsLazyPagingItems(),
        onItemClicked = {}
    )
}