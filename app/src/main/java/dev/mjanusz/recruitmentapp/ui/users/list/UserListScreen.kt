package dev.mjanusz.recruitmentapp.ui.users.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import dev.mjanusz.recruitmentapp.AppState
import dev.mjanusz.recruitmentapp.R
import dev.mjanusz.recruitmentapp.ui.model.User
import dev.mjanusz.recruitmentapp.ui.theme.AppTypography

@Composable
fun UserListScreen(
    appState: AppState,
    modifier: Modifier = Modifier,
    userListViewModel: UserListViewModel = hiltViewModel()
) {
    val lazyUserPagingItems = userListViewModel.userPagingFlow.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        userListViewModel.userClicked.collect {
            appState.navController.navigate("users/${it.login}") {
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    UserListContent(
        users = lazyUserPagingItems,
        clickListener = userListViewModel::onUserClicked,
        modifier = modifier
    )
}

@Composable
fun UserListContent(
    users: LazyPagingItems<User>,
    clickListener: (user: User) -> Unit = { },
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
    ) {
        items(
            count = users.itemCount,
            key = users.itemKey { it.id }
        ) { index ->
            users[index]?.run {
                UserRow(item = this, clickListener = clickListener)
            }
        }
    }
}

@Composable
fun UserRow(
    item: User,
    clickListener: (user: User) -> Unit
) {
    ListItem(
        modifier = Modifier.clickable {
            clickListener(item)
        },
        headlineContent = {
            Text(
                text = item.login,
                overflow = TextOverflow.Clip,
                maxLines = 1,
                style = AppTypography.titleLarge
            )
        },
        supportingContent = {
            Text(
                text = "#${item.id}",
                overflow = TextOverflow.Clip,
                maxLines = 1,
                style = AppTypography.titleSmall
            )
        },
        leadingContent = {
            AsyncImage(
                model = item.avatarUrl,
                contentDescription = stringResource(
                    R.string.content_desc_user_avatar, item.login
                ),
                placeholder = painterResource(R.drawable.drawable_gh_user_avatar_placeholder),
                modifier = Modifier.size(56.dp)
            )
        },
    )
}

@Composable
@Preview
fun PreviewUserRow() {
    UserRow(item = User(
        1, "maciekjanusz", "https://avatars.githubusercontent.com/u/8041839"
    ), clickListener = { })
}