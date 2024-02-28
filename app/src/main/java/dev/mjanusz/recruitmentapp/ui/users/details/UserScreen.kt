package dev.mjanusz.recruitmentapp.ui.users.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import dev.mjanusz.recruitmentapp.R
import dev.mjanusz.recruitmentapp.common.Failure
import dev.mjanusz.recruitmentapp.common.Loading
import dev.mjanusz.recruitmentapp.common.Success
import dev.mjanusz.recruitmentapp.data.local.model.UserDetails
import dev.mjanusz.recruitmentapp.ui.common.ErrorIndicatorWithRetry
import dev.mjanusz.recruitmentapp.ui.theme.AppTypography
import dev.mjanusz.recruitmentapp.ui.theme.largeElevation
import java.time.OffsetDateTime

// todo: rememberSaveable for configuration changes!

@Composable
fun UserScreen(
    username: String,
    userViewModel: UserViewModel = hiltViewModel()
) {
    val userDetailsState by userViewModel.userDetails.collectAsStateWithLifecycle()

    when (userDetailsState) {
        is Failure -> {
            val message = (userDetailsState as Failure<UserDetails>).throwable.message
            ErrorIndicatorWithRetry(
                message = "$message",
            ) {
                userViewModel.loadUserDetails(username)
            }
        }

        is Loading -> {
            LinearProgressIndicator(Modifier.fillMaxWidth())
            userViewModel.loadUserDetails(username)
        }

        is Success -> {
            UserContent(userDetails = (userDetailsState as Success<UserDetails>).value)
        }
    }
}

@Composable
fun UserContent(
    userDetails: UserDetails
) {
    Surface(
        shadowElevation = largeElevation,
        modifier = Modifier.padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            AsyncImage(
                model = userDetails.avatarUrl,
                contentDescription = stringResource(
                    id = R.string.content_desc_user_avatar, userDetails.login
                ),
                placeholder = painterResource(R.drawable.drawable_gh_user_avatar_placeholder),
                modifier = Modifier
                    .size(112.dp)
            )
            Text(text = userDetails.login, style = AppTypography.titleLarge)
            Text(text = userDetails.name, style = AppTypography.titleMedium)
            Text(text = userDetails.location, style = AppTypography.titleSmall)
            Text(text = userDetails.company, style = AppTypography.bodyMedium)
            Text(text = userDetails.email, style = AppTypography.bodyMedium)
            Text(text = userDetails.bio, style = AppTypography.bodySmall)
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun PreviewUserContent() {
    UserContent(
        userDetails = UserDetails(
            login = "username",
            id = 12345678,
            avatarUrl = "",
            name = "User Name",
            company = "Company",
            bio = """Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor 
            |incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.""".trimMargin(),
            location = "Place, ??",
            email = "user@host.domain",
            publicRepos = 100,
            publicGists = 200,
            followers = 300,
            following = 400,
            createdAt = OffsetDateTime.now().minusYears(10),
            updatedAt = OffsetDateTime.now(),
        )
    )
}
